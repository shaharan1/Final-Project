import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:flutter_hospital_management/providers/dashboard_provider.dart';
import 'package:flutter_hospital_management/providers/auth_provider.dart';
import 'package:flutter_hospital_management/widgets/common.dart';
import 'package:flutter_hospital_management/widgets/app_drawer.dart';
import 'package:flutter_hospital_management/models/dashboard.dart';
import 'package:fl_chart/fl_chart.dart';
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

  String _today() {
    final d = DateTime.now();
    const days = [
      'Monday',
      'Tuesday',
      'Wednesday',
      'Thursday',
      'Friday',
      'Saturday',
      'Sunday'
    ];
    const months = [
      'Jan',
      'Feb',
      'Mar',
      'Apr',
      'May',
      'Jun',
      'Jul',
      'Aug',
      'Sep',
      'Oct',
      'Nov',
      'Dec'
    ];
    return '${days[(d.weekday - 1) % 7]}, ${d.day} ${months[d.month - 1]} ${d.year}';
  }

  @override
  Widget build(BuildContext context) {
    final state = ref.watch(dashboardNotifierProvider);
    final user = ref.watch(authNotifierProvider).user;
    final b = state.billing;
    final p = state.payments;

    final palette = [
      AppTheme.primary,
      AppTheme.accent,
      AppTheme.info,
      AppTheme.warning,
      AppTheme.success,
      AppTheme.danger,
    ];

    return Scaffold(
      appBar: AppBar(
        title: const Text('Dashboard'),
        actions: [
          IconButton(
            icon: const Icon(Icons.refresh),
            tooltip: 'Refresh',
            onPressed: () =>
                ref.read(dashboardNotifierProvider.notifier).load(),
          )
        ],
      ),
      drawer: const AppDrawer(),
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
                    padding: EdgeInsets.zero,
                    children: [
                      _Hero(name: user?.name, date: _today()),
                      Padding(
                        padding: const EdgeInsets.all(16),
                        child: Column(
                          crossAxisAlignment: CrossAxisAlignment.start,
                          children: [
                            const SectionTitle('Financial Overview',
                                icon: Icons.account_balance_wallet),
                            _statGrid([
                              _StatCard(
                                'Total Revenue',
                                (b?.totalRevenue ?? 0).toDouble(),
                                icon: Icons.savings,
                                color: AppTheme.primary,
                              ),
                              _StatCard(
                                'Today Revenue',
                                (b?.todayRevenue ?? 0).toDouble(),
                                icon: Icons.today,
                                color: AppTheme.info,
                              ),
                              _StatCard(
                                'Collected',
                                (b?.totalCollected ?? 0).toDouble(),
                                icon: Icons.check_circle,
                                color: AppTheme.success,
                              ),
                              _StatCard(
                                'Total Due',
                                (b?.totalDue ?? 0).toDouble(),
                                icon: Icons.warning_amber,
                                color: AppTheme.danger,
                              ),
                            ]),
                            const SizedBox(height: 16),
                            _dual(
                              _SummaryCard(
                                title: 'Invoice Status',
                                icon: Icons.receipt_long,
                                children: [
                                  _StatusRow('Total Invoices',
                                      '${b?.totalInvoices ?? 0}', AppTheme.primary),
                                  _StatusRow('Paid', '${b?.paidCount ?? 0}',
                                      AppTheme.success),
                                  _StatusRow('Partial', '${b?.partialCount ?? 0}',
                                      AppTheme.warning),
                                  _StatusRow('Unpaid', '${b?.unpaidCount ?? 0}',
                                      AppTheme.danger),
                                ],
                              ),
                              _CollectionCard(
                                collected: (b?.totalCollected ?? 0).toDouble(),
                                due: (b?.totalDue ?? 0).toDouble(),
                              ),
                            ),
                            const SizedBox(height: 24),
                            const SectionTitle('Payments',
                                icon: Icons.payments),
                            _statGrid([
                              _StatCard('Transactions',
                                  _toDouble(p?['totalTransactions']),
                                  icon: Icons.swap_horiz, color: AppTheme.primary),
                              _StatCard('Completed',
                                  _toDouble(p?['completedPayments']),
                                  icon: Icons.verified, color: AppTheme.success),
                              _StatCard('Pending',
                                  _toDouble(p?['pendingPayments']),
                                  icon: Icons.hourglass_bottom,
                                  color: AppTheme.warning),
                              _StatCard('Failed',
                                  _toDouble(p?['failedPayments']),
                                  icon: Icons.error_outline, color: AppTheme.danger),
                              _StatCard('Today Revenue',
                                  _toDouble(p?['todayRevenue']),
                                  icon: Icons.today, color: AppTheme.info),
                              _StatCard('Avg Payment',
                                  _toDouble(p?['averagePaymentAmount']),
                                  icon: Icons.insights, color: AppTheme.primaryDark),
                            ]),
                            if (b != null &&
                                b.paymentMethodBreakdown.isNotEmpty) ...[
                              const SizedBox(height: 24),
                              const SectionTitle('Revenue by Payment Method',
                                  icon: Icons.donut_large),
                              AppCard(
                                child: Column(
                                  children: [
                                    for (var i = 0;
                                        i < b.paymentMethodBreakdown.length;
                                        i++)
                                      MiniBar(
                                        b.paymentMethodBreakdown[i].method ?? '',
                                        b.paymentMethodBreakdown[i].totalAmount ??
                                            0,
                                        b.paymentMethodBreakdown
                                            .map((e) => e.totalAmount ?? 0)
                                            .reduce((a, c) => a > c ? a : c),
                                        palette[i % palette.length],
                                        prefix: '৳ ',
                                      ),
                                  ],
                                ),
                              ),
                            ],
                            if (b != null && b.revenueByCategory.isNotEmpty) ...[
                              const SizedBox(height: 24),
                              const SectionTitle('Revenue by Category',
                                  icon: Icons.category),
                              AppCard(
                                child: Column(
                                  children: [
                                    for (var i = 0;
                                        i < b.revenueByCategory.length;
                                        i++)
                                      MiniBar(
                                        b.revenueByCategory[i].categoryName ??
                                            b.revenueByCategory[i].categoryCode ??
                                            '',
                                        b.revenueByCategory[i].totalAmount ?? 0,
                                        b.revenueByCategory
                                            .map((e) => e.totalAmount ?? 0)
                                            .reduce((a, c) => a > c ? a : c),
                                        AppTheme.primary,
                                        prefix: '৳ ',
                                      ),
                                  ],
                                ),
                              ),
                            ],
                            if (b != null && b.dailyRevenueChart.isNotEmpty) ...[
                              const SizedBox(height: 24),
                              const SectionTitle('Revenue Trend',
                                  icon: Icons.show_chart),
                              AppCard(child: _revenueLineChart(b)),
                            ],
                            if (p != null) ...[
                              const SizedBox(height: 24),
                              const SectionTitle('Payment Status',
                                  icon: Icons.pie_chart),
                              AppCard(child: _paymentStatusPie(p)),
                            ],
                            const SizedBox(height: 24),
                          ],
                        ),
                      ),
                    ],
                  ),
                ),
    );
  }

  Widget _dual(Widget a, Widget b) => LayoutBuilder(
        builder: (_, c) => c.maxWidth < 600
            ? Column(children: [a, const SizedBox(height: 12), b])
            : Row(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                    Expanded(child: a),
                    const SizedBox(width: 12),
                    Expanded(child: b),
                  ]),
      );

  Widget _statGrid(List<Widget> cards) => LayoutBuilder(
        builder: (_, c) {
          final w = c.maxWidth;
          final cols = w > 820 ? 3 : 2;
          final gap = 12.0;
          final cw = (w - gap * (cols - 1)) / cols;
          return Wrap(
            spacing: gap,
            runSpacing: gap,
            children: cards
                .map((c) => SizedBox(width: cw, child: c))
                .toList(),
          );
        },
      );

  double _toDouble(dynamic v) => (v is num) ? v.toDouble() : 0.0;

  Widget _revenueLineChart(BillingDashboard b) {
    final data = b.dailyRevenueChart;
    if (data.isEmpty) return const SizedBox();
    final spots = data
        .asMap()
        .entries
        .map((e) =>
            FlSpot(e.key.toDouble(), (e.value.revenue ?? 0).toDouble()))
        .toList();
    final maxY = data
        .map((e) => (e.revenue ?? 0).toDouble())
        .fold(0.0, (a, c) => a > c ? a : c);
    return SizedBox(
      height: 240,
      child: LineChart(
        LineChartData(
          minY: 0,
          maxY: maxY * 1.2,
          lineBarsData: [
            LineChartBarData(
              spots: spots,
              isCurved: true,
              color: AppTheme.primary,
              barWidth: 3,
              dotData: FlDotData(show: false),
              belowBarData: BarAreaData(
                show: true,
                color: AppTheme.primary.withValues(alpha: 0.15),
              ),
            ),
          ],
          gridData: FlGridData(show: true, drawVerticalLine: false),
          borderData: FlBorderData(show: false),
          titlesData: FlTitlesData(
            leftTitles:
                AxisTitles(sideTitles: SideTitles(showTitles: false)),
            rightTitles:
                AxisTitles(sideTitles: SideTitles(showTitles: false)),
            topTitles:
                AxisTitles(sideTitles: SideTitles(showTitles: false)),
            bottomTitles: AxisTitles(
              sideTitles: SideTitles(
                showTitles: true,
                getTitlesWidget: (v, meta) {
                  final i = v.toInt();
                  if (i < 0 || i >= data.length) return const SizedBox();
                  final d = data[i].date ?? '';
                  return Padding(
                    padding: const EdgeInsets.only(top: 6),
                    child: Text(d.length >= 5 ? d.substring(5) : d,
                        style: const TextStyle(fontSize: 10)),
                  );
                },
              ),
            ),
          ),
        ),
      ),
    );
  }

  Widget _paymentStatusPie(Map<String, dynamic>? p) {
    if (p == null) return const SizedBox();
    final items = [
      ('Completed', _toDouble(p['completedPayments']), AppTheme.success),
      ('Pending', _toDouble(p['pendingPayments']), AppTheme.warning),
      ('Failed', _toDouble(p['failedPayments']), AppTheme.danger),
    ];
    final total = items.fold(0.0, (s, e) => s + e.$2);
    if (total <= 0) return const SizedBox();
    return SizedBox(
      height: 220,
      child: PieChart(
        PieChartData(
          sectionsSpace: 3,
          centerSpaceRadius: 45,
          sections: items
              .map((e) => PieChartSectionData(
                    value: e.$2,
                    color: e.$3,
                    title: '${(e.$2 / total * 100).toStringAsFixed(0)}%',
                    radius: 80,
                    titleStyle: const TextStyle(
                        color: Colors.white,
                        fontSize: 12,
                        fontWeight: FontWeight.bold),
                  ))
              .toList(),
        ),
      ),
    );
  }
}

class _Hero extends StatelessWidget {
  final String? name;
  final String date;
  const _Hero({this.name, required this.date});

  @override
  Widget build(BuildContext context) => Container(
        width: double.infinity,
        decoration: const BoxDecoration(gradient: AppTheme.primaryGradient),
        padding: const EdgeInsets.fromLTRB(20, 22, 20, 26),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Text(
              name != null ? 'Hello, $name 👋' : 'Welcome 👋',
              style: const TextStyle(
                  color: Colors.white,
                  fontSize: 22,
                  fontWeight: FontWeight.bold),
            ),
            const SizedBox(height: 6),
            Row(
              children: [
                const Icon(Icons.calendar_today,
                    color: Colors.white70, size: 15),
                const SizedBox(width: 6),
                Text(date,
                    style: const TextStyle(color: Colors.white70, fontSize: 13)),
                const Spacer(),
                const Text('Hospital Dashboard',
                    style: TextStyle(color: Colors.white, fontSize: 13)),
              ],
            ),
          ],
        ),
      );
}

class _StatCard extends StatelessWidget {
  final String label;
  final double value;
  final IconData icon;
  final Color color;

  const _StatCard(this.label, this.value,
      {required this.icon, required this.color});

  @override
  Widget build(BuildContext context) => Container(
        padding: const EdgeInsets.all(16),
        decoration: BoxDecoration(
          color: Colors.white,
          borderRadius: BorderRadius.circular(16),
          boxShadow: const [
            BoxShadow(
                color: Color(0x14000000),
                blurRadius: 10,
                offset: Offset(0, 4))
          ],
          border: Border.all(color: color.withValues(alpha: 0.18)),
        ),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Container(
              padding: const EdgeInsets.all(8),
              decoration: BoxDecoration(
                color: color.withValues(alpha: 0.12),
                borderRadius: BorderRadius.circular(10),
              ),
              child: Icon(icon, color: color, size: 20),
            ),
            const SizedBox(height: 14),
            AnimatedCounter(value,
                prefix: '৳ ',
                style: const TextStyle(
                    fontSize: 22,
                    fontWeight: FontWeight.bold,
                    color: Color(0xFF263238)),
                format: (v) =>
                    v % 1 == 0 ? v.toInt().toString() : v.toStringAsFixed(2)),
            const SizedBox(height: 4),
            Text(label,
                style: const TextStyle(fontSize: 12, color: Colors.grey)),
          ],
        ),
      );
}

class _SummaryCard extends StatelessWidget {
  final String title;
  final IconData icon;
  final List<Widget> children;

  const _SummaryCard(
      {required this.title, required this.icon, required this.children});

  @override
  Widget build(BuildContext context) => AppCard(
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Row(
              children: [
                Icon(icon, color: AppTheme.primary, size: 20),
                const SizedBox(width: 8),
                Text(title,
                    style: const TextStyle(
                        fontSize: 15, fontWeight: FontWeight.bold)),
              ],
            ),
            const SizedBox(height: 12),
            ...children,
          ],
        ),
      );
}

class _StatusRow extends StatelessWidget {
  final String label;
  final String value;
  final Color color;
  const _StatusRow(this.label, this.value, this.color);

  @override
  Widget build(BuildContext context) => Padding(
        padding: const EdgeInsets.symmetric(vertical: 7),
        child: Row(
          children: [
            Container(
              width: 10,
              height: 10,
              decoration: BoxDecoration(
                color: color,
                borderRadius: BorderRadius.circular(3),
              ),
            ),
            const SizedBox(width: 10),
            Expanded(
                child: Text(label,
                    style: const TextStyle(fontSize: 13, color: Colors.grey))),
            Text(value,
                style: const TextStyle(
                    fontSize: 15, fontWeight: FontWeight.bold)),
          ],
        ),
      );
}

class _CollectionCard extends StatelessWidget {
  final double collected;
  final double due;
  const _CollectionCard({required this.collected, required this.due});

  @override
  Widget build(BuildContext context) {
    final total = collected + due;
    final percent = total > 0 ? collected / total : 0.0;
    final color = percent > 0.75
        ? AppTheme.success
        : percent > 0.4
            ? AppTheme.warning
            : AppTheme.danger;
    return AppCard(
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Row(
            children: [
              Icon(Icons.pie_chart, color: AppTheme.primary, size: 20),
              const SizedBox(width: 8),
              const Text('Collection Rate',
                  style: TextStyle(fontSize: 15, fontWeight: FontWeight.bold)),
            ],
          ),
          const SizedBox(height: 14),
          Center(
            child: SizedBox(
              width: 110,
              height: 110,
              child: Stack(
                alignment: Alignment.center,
                children: [
                  Transform.rotate(
                    angle: -3.14159 / 2,
                    child: SizedBox(
                      width: 110,
                      height: 110,
                      child: CircularProgressIndicator(
                        value: percent,
                        strokeWidth: 11,
                        backgroundColor: color.withValues(alpha: 0.12),
                        valueColor: AlwaysStoppedAnimation(color),
                      ),
                    ),
                  ),
                  Column(
                    mainAxisSize: MainAxisSize.min,
                    children: [
                      Text('${(percent * 100).toInt()}%',
                          style: TextStyle(
                              fontSize: 24,
                              fontWeight: FontWeight.bold,
                              color: color)),
                      const Text('collected',
                          style: TextStyle(fontSize: 12, color: Colors.grey)),
                    ],
                  ),
                ],
              ),
            ),
          ),
          const SizedBox(height: 12),
          Row(
            mainAxisAlignment: MainAxisAlignment.spaceAround,
            children: [
              _Legend('Collected', '৳ ${collected % 1 == 0 ? collected.toInt() : collected.toStringAsFixed(2)}', AppTheme.success),
              _Legend('Due', '৳ ${due % 1 == 0 ? due.toInt() : due.toStringAsFixed(2)}', AppTheme.danger),
            ],
          ),
        ],
      ),
    );
  }
}

class _Legend extends StatelessWidget {
  final String label;
  final String value;
  final Color color;
  const _Legend(this.label, this.value, this.color);

  @override
  Widget build(BuildContext context) => Column(
        children: [
          Row(
            mainAxisSize: MainAxisSize.min,
            children: [
              Container(
                width: 9,
                height: 9,
                decoration: BoxDecoration(
                  color: color,
                  borderRadius: BorderRadius.circular(3),
                ),
              ),
              const SizedBox(width: 6),
              Text(label, style: const TextStyle(fontSize: 12, color: Colors.grey)),
            ],
          ),
          const SizedBox(height: 4),
          Text(value,
              style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 13)),
        ],
      );
}
