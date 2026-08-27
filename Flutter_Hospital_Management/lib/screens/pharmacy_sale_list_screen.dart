import 'package:flutter/material.dart';
import 'package:flutter_hospital_management/widgets/app_drawer.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:flutter_hospital_management/providers/pharmacy_provider.dart';
import 'package:flutter_hospital_management/screens/pharmacy_sale_detail_screen.dart';
import 'package:flutter_hospital_management/widgets/common.dart';

class PharmacySaleListScreen extends ConsumerStatefulWidget {
  const PharmacySaleListScreen({super.key});

  @override
  ConsumerState<PharmacySaleListScreen> createState() =>
      _PharmacySaleListScreenState();
}

class _PharmacySaleListScreenState
    extends ConsumerState<PharmacySaleListScreen> {
  @override
  void initState() {
    super.initState();
    Future.microtask(() => ref.read(saleNotifierProvider.notifier).load());
  }

  String _money(dynamic v) =>
      '৳ ${(v is num ? v.toDouble() : 0).toStringAsFixed(2)}';

  @override
  Widget build(BuildContext context) {
    final state = ref.watch(saleNotifierProvider);
    return Scaffold(
      appBar: AppBar(title: const Text('Pharmacy Sales')),
      drawer: const AppDrawer(),
      body: state.isLoading
          ? const Center(child: CircularProgressIndicator())
          : state.error != null
              ? Center(child: Text(state.error!, style: const TextStyle(color: Colors.red)))
              : state.sales.isEmpty
                  ? const EmptyState('No sales yet')
                  : RefreshIndicator(
                      onRefresh: () =>
                          ref.read(saleNotifierProvider.notifier).load(),
                      child: ListView.separated(
                        padding: const EdgeInsets.all(16),
                        itemCount: state.sales.length,
                        separatorBuilder: (_, _) => const SizedBox(height: 10),
                        itemBuilder: (_, i) {
                          final s = state.sales[i];
                          return AppCard(
                            onTap: () => Navigator.push(
                              context,
                              MaterialPageRoute(
                                  builder: (_) =>
                                      PharmacySaleDetailScreen(sale: s)),
                            ),
                            child: Column(
                              crossAxisAlignment: CrossAxisAlignment.start,
                              children: [
                                Row(
                                  mainAxisAlignment:
                                      MainAxisAlignment.spaceBetween,
                                  children: [
                                    Text(s.saleInvoiceNo ?? 'Sale #${s.id}',
                                        style: const TextStyle(
                                            fontWeight: FontWeight.w700,
                                            fontSize: 15)),
                                    StatusChip.fromStatus(
                                        s.paymentStatus ?? 'N/A'),
                                  ],
                                ),
                                const SizedBox(height: 6),
                                Text(
                                    s.patientName ?? s.patientType ?? 'Walk-in',
                                    style: const TextStyle(
                                        fontWeight: FontWeight.w500)),
                                const SizedBox(height: 4),
                                Row(
                                  mainAxisAlignment:
                                      MainAxisAlignment.spaceBetween,
                                  children: [
                                    Text('Net: ${_money(s.netPayable)}',
                                        style: const TextStyle(
                                            color: Colors.grey, fontSize: 13)),
                                    Text('Paid: ${_money(s.paidAmount)}',
                                        style: const TextStyle(
                                            color: Color(0xFF2E9E5B),
                                            fontSize: 13,
                                            fontWeight: FontWeight.w600)),
                                  ],
                                ),
                              ],
                            ),
                          );
                        },
                      ),
                    ),
    );
  }
}
