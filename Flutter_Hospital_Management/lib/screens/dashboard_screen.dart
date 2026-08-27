import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:flutter_hospital_management/providers/dashboard_provider.dart';

class DashboardScreen extends ConsumerStatefulWidget {
  const DashboardScreen({super.key});

  @override
  ConsumerState<DashboardScreen> createState() => _DashboardScreenState();
}

class _DashboardScreenState extends ConsumerState<DashboardScreen> {
  @override
  void initState() {
    super.initState();
    Future.microtask(
        () => ref.read(dashboardNotifierProvider.notifier).load());
  }

  String _num(dynamic v) {
    if (v == null) return '0';
    if (v is double) return v.toStringAsFixed(2);
    return v.toString();
  }

  @override
  Widget build(BuildContext context) {
    final state = ref.watch(dashboardNotifierProvider);
    final b = state.billing;
    final p = state.payments;

    return Scaffold(
      appBar: AppBar(
        title: const Text('Dashboard'),
        backgroundColor: Colors.teal,
        foregroundColor: Colors.white,
      ),
      body: state.isLoading
          ? const Center(child: CircularProgressIndicator())
          : state.error != null
              ? Center(child: Text(state.error!, style: const TextStyle(color: Colors.red)))
              : RefreshIndicator(
                  onRefresh: () =>
                      ref.read(dashboardNotifierProvider.notifier).load(),
                  child: ListView(
                    padding: const EdgeInsets.all(16),
                    children: [
                      const Text('Billing',
                          style: TextStyle(fontWeight: FontWeight.bold, fontSize: 18)),
                      const SizedBox(height: 10),
                      GridView.count(
                        shrinkWrap: true,
                        physics: const NeverScrollableScrollPhysics(),
                        crossAxisCount: 2,
                        mainAxisSpacing: 12,
                        crossAxisSpacing: 12,
                        childAspectRatio: 1.8,
                        children: [
                          _StatCard('Total Revenue', _num(b?.totalRevenue), Colors.teal),
                          _StatCard('Today Revenue', _num(b?.todayRevenue), Colors.indigo),
                          _StatCard('Collected', _num(b?.totalCollected), Colors.green),
                          _StatCard('Total Due', _num(b?.totalDue), Colors.red),
                          _StatCard('Total Invoices', _num(b?.totalInvoices), Colors.deepPurple),
                          _StatCard('Paid / Partial / Unpaid',
                              '${b?.paidCount ?? 0} / ${b?.partialCount ?? 0} / ${b?.unpaidCount ?? 0}',
                              Colors.blueGrey),
                        ],
                      ),
                      const SizedBox(height: 24),
                      const Text('Payments',
                          style: TextStyle(fontWeight: FontWeight.bold, fontSize: 18)),
                      const SizedBox(height: 10),
                      GridView.count(
                        shrinkWrap: true,
                        physics: const NeverScrollableScrollPhysics(),
                        crossAxisCount: 2,
                        mainAxisSpacing: 12,
                        crossAxisSpacing: 12,
                        childAspectRatio: 1.8,
                        children: [
                          _StatCard('Total Transactions', _num(p?['totalTransactions']), Colors.teal),
                          _StatCard('Completed', _num(p?['completedPayments']), Colors.green),
                          _StatCard('Pending', _num(p?['pendingPayments']), Colors.orange),
                          _StatCard('Failed', _num(p?['failedPayments']), Colors.red),
                          _StatCard('Today Revenue', _num(p?['todayRevenue']), Colors.indigo),
                          _StatCard('Avg Payment', _num(p?['averagePaymentAmount']), Colors.deepPurple),
                        ],
                      ),
                      const SizedBox(height: 24),
                      if (b != null && b.paymentMethodBreakdown.isNotEmpty) ...[
                        const Text('Payment Methods',
                            style: TextStyle(fontWeight: FontWeight.bold, fontSize: 16)),
                        const SizedBox(height: 8),
                        ...b.paymentMethodBreakdown.map((m) => ListTile(
                              title: Text(m.method ?? ''),
                              trailing: Text('${_num(m.totalAmount)}  (${m.count ?? 0})'),
                            )),
                      ],
                      const SizedBox(height: 16),
                      if (b != null && b.revenueByCategory.isNotEmpty) ...[
                        const Text('Revenue by Category',
                            style: TextStyle(fontWeight: FontWeight.bold, fontSize: 16)),
                        const SizedBox(height: 8),
                        ...b.revenueByCategory.map((c) => ListTile(
                              title: Text(c.categoryName ?? c.categoryCode ?? ''),
                              trailing: Text(_num(c.totalAmount)),
                            )),
                      ],
                    ],
                  ),
                ),
    );
  }
}

class _StatCard extends StatelessWidget {
  final String label;
  final String value;
  final Color color;

  const _StatCard(this.label, this.value, this.color);

  @override
  Widget build(BuildContext context) {
    return Card(
      elevation: 2,
      child: Padding(
        padding: const EdgeInsets.all(12),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Text(label,
                style: const TextStyle(fontSize: 13, color: Colors.grey)),
            const SizedBox(height: 6),
            Text(value,
                style: TextStyle(
                    fontSize: 20,
                    fontWeight: FontWeight.bold,
                    color: color)),
          ],
        ),
      ),
    );
  }
}
