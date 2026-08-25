import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:flutter_hospital_management/providers/billing_provider.dart';
import 'package:flutter_hospital_management/screens/billing_invoice_form_screen.dart';
import 'package:flutter_hospital_management/screens/billing_invoice_detail_screen.dart';

class BillingInvoiceListScreen extends ConsumerStatefulWidget {
  const BillingInvoiceListScreen({super.key});

  @override
  ConsumerState<BillingInvoiceListScreen> createState() =>
      _BillingInvoiceListScreenState();
}

class _BillingInvoiceListScreenState
    extends ConsumerState<BillingInvoiceListScreen> {
  final _search = TextEditingController();

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

  @override
  Widget build(BuildContext context) {
    final state = ref.watch(billingNotifierProvider);

    return Scaffold(
      appBar: AppBar(
        title: const Text('Billing'),
        backgroundColor: Colors.teal,
        foregroundColor: Colors.white,
      ),
      body: Column(
        children: [
          Padding(
            padding: const EdgeInsets.all(12),
            child: TextField(
              controller: _search,
              decoration: InputDecoration(
                labelText: 'Search (invoice no, patient)',
                prefixIcon: const Icon(Icons.search),
                border: const OutlineInputBorder(),
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
          if (state.error != null)
            Padding(
              padding: const EdgeInsets.symmetric(horizontal: 12),
              child:
                  Text(state.error!, style: const TextStyle(color: Colors.red)),
            ),
          if (state.isLoading)
            const Expanded(child: Center(child: CircularProgressIndicator()))
          else if (state.invoices.isEmpty)
            const Expanded(child: Center(child: Text('No invoices found.')))
          else
            Expanded(
              child: ListView.separated(
                itemCount: state.invoices.length,
                separatorBuilder: (_, _) => const Divider(height: 1),
                itemBuilder: (context, i) {
                  final inv = state.invoices[i];
                  return ListTile(
                    title: Text(inv.invoiceNumber ?? '#$i'),
                    subtitle: Text(
                        '${inv.patientName ?? 'Unknown'}  •  ${inv.invoiceType ?? ''}\nNet: ${inv.netAmount ?? 0}  •  Due: ${inv.dueAmount ?? 0}'),
                    isThreeLine: true,
                    trailing: Chip(
                      label: Text(inv.paymentStatus ?? ''),
                      backgroundColor:
                          inv.paymentStatus == 'PAID' ? Colors.green[100] : Colors.orange[100],
                    ),
                    onTap: () => Navigator.push(
                      context,
                      MaterialPageRoute(
                        builder: (_) =>
                            BillingInvoiceDetailScreen(invoiceId: inv.id!),
                      ),
                    ),
                  );
                },
              ),
            ),
        ],
      ),
      floatingActionButton: FloatingActionButton(
        backgroundColor: Colors.teal,
        foregroundColor: Colors.white,
        child: const Icon(Icons.add),
        onPressed: () => Navigator.push(
          context,
          MaterialPageRoute(builder: (_) => const BillingInvoiceFormScreen()),
        ),
      ),
    );
  }
}
