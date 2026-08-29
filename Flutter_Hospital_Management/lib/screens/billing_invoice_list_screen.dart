import 'package:flutter/material.dart';
import 'package:flutter_hospital_management/widgets/bottom_nav.dart';
import 'package:flutter_hospital_management/widgets/app_drawer.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:flutter_hospital_management/providers/billing_provider.dart';
import 'package:flutter_hospital_management/screens/billing_invoice_form_screen.dart';
import 'package:flutter_hospital_management/screens/billing_invoice_detail_screen.dart';
import 'package:flutter_hospital_management/widgets/common.dart';

class BillingInvoiceListScreen extends ConsumerStatefulWidget {
  const BillingInvoiceListScreen({super.key});

  @override
  ConsumerState<BillingInvoiceListScreen> createState() =>
      _BillingInvoiceListScreenState();
}

class _BillingInvoiceListScreenState
    extends ConsumerState<BillingInvoiceListScreen> {
  final _search = TextEditingController();
  String _filter = 'All';

  @override
  void initState() {
    super.initState();
    Future.microtask(
        () => ref.read(billingNotifierProvider.notifier).load());
  }

  @override
  void dispose() {
    _search.dispose();
    super.dispose();
  }

  String _money(dynamic v) =>
      '৳ ${(v is num ? v.toDouble() : 0).toStringAsFixed(2)}';

  Widget _buildChips(List<String> chips) => SingleChildScrollView(
        scrollDirection: Axis.horizontal,
        padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 4),
        child: Row(
          children: chips
              .map((c) => Padding(
                    padding: const EdgeInsets.only(right: 8),
                    child: ChoiceChip(
                      label: Text(c),
                      selected: _filter == c,
                      onSelected: (_) => setState(() => _filter = c),
                    ),
                  ))
              .toList(),
        ),
      );

  @override
  Widget build(BuildContext context) {
    final state = ref.watch(billingNotifierProvider);
    final statuses = {
      for (final inv in state.invoices)
        if (inv.paymentStatus != null && inv.paymentStatus!.isNotEmpty)
          inv.paymentStatus!
    }.toList()
      ..sort();
    final chips = ['All', ...statuses];
    final filtered = _filter == 'All'
        ? state.invoices
        : state.invoices
            .where((inv) => (inv.paymentStatus ?? '') == _filter)
            .toList();

    return Scaffold(
      bottomNavigationBar: const RoleBottomNav(currentKey: 'billing'),
      appBar: AppBar(title: const Text('Billing')),
      drawer: const AppDrawer(),
      floatingActionButton: FloatingActionButton(
        child: const Icon(Icons.add),
        onPressed: () => Navigator.push(
          context,
          MaterialPageRoute(builder: (_) => const BillingInvoiceFormScreen()),
        ).then((_) => ref.read(billingNotifierProvider.notifier).load()),
      ),
      body: Column(
        children: [
          Padding(
            padding: const EdgeInsets.all(16),
            child: TextField(
              controller: _search,
              decoration: InputDecoration(
                labelText: 'Search (invoice no, patient)',
                prefixIcon: const Icon(Icons.search),
                suffixIcon: IconButton(
                  icon: const Icon(Icons.clear),
                  onPressed: () {
                    _search.clear();
                    ref.read(billingNotifierProvider.notifier).load();
                  },
                ),
              ),
                  onChanged: (v) =>
                      ref.read(billingNotifierProvider.notifier).search(v.trim()),
            ),
          ),
          _buildChips(chips),
          if (state.error != null)
            Padding(
              padding: const EdgeInsets.symmetric(horizontal: 16),
              child: Text(state.error!,
                  style: const TextStyle(color: Colors.red)),
            ),
          if (state.isLoading)
            const Expanded(child: Center(child: CircularProgressIndicator()))
          else if (filtered.isEmpty)
            const Expanded(
                child: EmptyState('No invoices found',
                    icon: Icons.receipt_long))
          else
            Expanded(
              child: RefreshIndicator(
                onRefresh: () =>
                    ref.read(billingNotifierProvider.notifier).load(),
                child: ListView.separated(
                  padding: const EdgeInsets.symmetric(horizontal: 16),
                  itemCount: filtered.length,
                  separatorBuilder: (_, _) => const SizedBox(height: 10),
                  itemBuilder: (context, i) {
                    final inv = filtered[i];
                    return AppCard(
                      onTap: () => Navigator.push(
                        context,
                        MaterialPageRoute(
                          builder: (_) =>
                              BillingInvoiceDetailScreen(invoiceId: inv.id!),
                        ),
                      ),
                      child: Row(
                        children: [
                          CircleAvatar(
                            backgroundColor: const Color(0xFF0E7C86)
                                .withValues(alpha: 0.12),
                            child: const Icon(Icons.receipt_long,
                                color: Color(0xFF0E7C86)),
                          ),
                          const SizedBox(width: 14),
                          Expanded(
                            child: Column(
                              crossAxisAlignment: CrossAxisAlignment.start,
                              children: [
                                Text(inv.invoiceNumber ?? '#$i',
                                    style: const TextStyle(
                                        fontWeight: FontWeight.w600,
                                        fontSize: 15)),
                                const SizedBox(height: 4),
                                Text(
                                  '${inv.patientName ?? 'Unknown'}  •  ${inv.invoiceType ?? ''}',
                                  style: const TextStyle(
                                      fontSize: 13, color: Colors.grey),
                                ),
                                const SizedBox(height: 4),
                                Text(
                                  'Net: ${_money(inv.netAmount)}  •  Due: ${_money(inv.dueAmount)}',
                                  style: const TextStyle(
                                      fontSize: 12, color: Colors.grey),
                                ),
                              ],
                            ),
                          ),
                          StatusChip.fromStatus(inv.paymentStatus ?? 'N/A'),
                        ],
                      ),
                    );
                  },
                ),
              ),
            ),
        ],
      ),
    );
  }
}
