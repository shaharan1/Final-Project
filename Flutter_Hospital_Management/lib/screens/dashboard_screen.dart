import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:flutter_hospital_management/providers/dashboard_provider.dart';
import 'package:flutter_hospital_management/widgets/common.dart';
import 'package:flutter_hospital_management/theme.dart';

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

  @override
  Widget build(BuildContext context) {
    final state = ref.watch(dashboardNotifierProvider);
    final b = state.billing;
    final p = state.payments;

    return Scaffold(
      appBar: AppBar(
        title: const Text('Dashboard'),
        actions: [
          IconButton(
            icon: const Icon(Icons.refresh),
            onPressed: () =>
                ref.read(dashboardNotifierProvider.notifier).load(),
          )
        ],
      ),
      body: state.isLoading
          ? const Center(child: CircularProgressIndicator())
          : state.error != null
              ? Center(
                  child: Padding(
                  padding: const EdgeInsets.all(24),
                  child: Text(state.error!,
                      style: const TextStyle(color: AppTheme.danger)),
                ))
              : RefreshIndicator(
                  onRefresh: () =>
                      ref.read(dashboardNotifierProvider.notifier).load(),
                  child: ListView(
                    padding: const EdgeInsets.all(16),
                    children: [
                      const SectionTitle('Billing Overview',
                          icon: Icons.account_balance_wallet),
                      Wrap(
                        spacing: 12,
                        runSpacing: 12,
                        children: [
                          _StatCard('Total Revenue',
                              (b?.totalRevenue ?? 0).toDouble(),
                              prefix: '৳ ', color: AppTheme.primary),
                          _StatCard('Today Revenue',
                              (b?.todayRevenue ?? 0).toDouble(),
                              prefix: '৳ ', color: AppTheme.info),
                          _StatCard('Collected',
                              (b?.totalCollected ?? 0).toDouble(),
                              prefix: '৳ ', color: AppTheme.success),
                          _StatCard('Total Due',
                              (b?.totalDue ?? 0).toDouble(),
                              prefix: '৳ ', color: AppTheme.danger),
                        ],
                      ),
                      const SizedBox(height: 12),
                      AppCard(
                        child: Row(
                          mainAxisAlignment: MainAxisAlignment.spaceAround,
                          children: [
                            _Mini(label: 'Invoices', value: '${b?.totalInvoices ?? 0}'),
                            _Mini(label: 'Paid', value: '${b?.paidCount ?? 0}', color: AppTheme.success),
                            _Mini(label: 'Partial', value: '${b?.partialCount ?? 0}', color: AppTheme.warning),
                            _Mini(label: 'Unpaid', value: '${b?.unpaidCount ?? 0}', color: AppTheme.danger),
                          ],
                        ),
                      ),
                      const SizedBox(height: 24),
                      const SectionTitle('Payments',
                          icon: Icons.payments),
                      GridView.count(
                        shrinkWrap: true,
                        physics: const NeverScrollableScrollPhysics(),
                        crossAxisCount: 2,
                        mainAxisSpacing: 12,
                        crossAxisSpacing: 12,
                        childAspectRatio: 1.9,
                        children: [
                          _StatCard('Transactions',
                              _toDouble(p?['totalTransactions']), color: AppTheme.primary),
                          _StatCard('Completed',
                              _toDouble(p?['completedPayments']), color: AppTheme.success),
                          _StatCard('Pending',
                              _toDouble(p?['pendingPayments']), color: AppTheme.warning),
                          _StatCard('Failed',
                              _toDouble(p?['failedPayments']), color: AppTheme.danger),
                          _StatCard('Today Rev',
                              _toDouble(p?['todayRevenue']), prefix: '৳ ', color: AppTheme.info),
                          _StatCard('Avg Payment',
                              _toDouble(p?['averagePaymentAmount']), prefix: '৳ ', color: AppTheme.primaryDark),
                        ],
                      ),
                      if (b != null &&
                          b.paymentMethodBreakdown.isNotEmpty) ...[
                        const SizedBox(height: 24),
                        const SectionTitle('Revenue by Payment Method',
                            icon: Icons.donut_large),
                        AppCard(
                          child: Column(
                            children: b.paymentMethodBreakdown
                                .map((m) => MiniBar(
                                      m.method ?? '',
                                      m.totalAmount ?? 0,
                                      b.paymentMethodBreakdown
                                              .map((e) => e.totalAmount ?? 0)
                                              .reduce((a, c) => a > c ? a : c),
                                      AppTheme.accent,
                                    ))
                                .toList(),
                          ),
                        ),
                      ],
                      if (b != null && b.revenueByCategory.isNotEmpty) ...[
                        const SizedBox(height: 24),
                        const SectionTitle('Revenue by Category',
                            icon: Icons.category),
                        AppCard(
                          child: Column(
                            children: b.revenueByCategory
                                .map((c) => MiniBar(
                                      c.categoryName ?? c.categoryCode ?? '',
                                      c.totalAmount ?? 0,
                                      b.revenueByCategory
                                          .map((e) => e.totalAmount ?? 0)
                                          .reduce((a, c) => a > c ? a : c),
                                      AppTheme.primary,
                                    ))
                                .toList(),
                          ),
                        ),
                      ],
                    ],
                  ),
                ),
    );
  }

  double _toDouble(dynamic v) => (v is num) ? v.toDouble() : 0.0;
}

class _StatCard extends StatelessWidget {
  final String label;
  final double value;
  final String prefix;
  final Color color;

  const _StatCard(this.label, this.value,
      {this.prefix = '', required this.color});

  @override
  Widget build(BuildContext context) => Container(
        padding: const EdgeInsets.all(14),
        decoration: BoxDecoration(
          color: Colors.white,
          borderRadius: BorderRadius.circular(16),
          boxShadow: const [
            BoxShadow(
                color: Color(0x1A000000), blurRadius: 8, offset: Offset(0, 3))
          ],
        ),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Text(label,
                style: const TextStyle(fontSize: 12, color: Colors.grey)),
            const SizedBox(height: 6),
            AnimatedCounter(value,
                prefix: prefix,
                format: (v) =>
                    v % 1 == 0 ? v.toInt().toString() : v.toStringAsFixed(2),
                ),
          ],
        ),
      );
}

class _Mini extends StatelessWidget {
  final String label;
  final String value;
  final Color? color;

  const _Mini({required this.label, required this.value, this.color});

  @override
  Widget build(BuildContext context) => Column(
        children: [
          Text(value,
              style: TextStyle(
                  fontSize: 20,
                  fontWeight: FontWeight.bold,
                  color: color ?? AppTheme.primary)),
          const SizedBox(height: 4),
          Text(label, style: const TextStyle(fontSize: 12, color: Colors.grey)),
        ],
      );
}
