import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:flutter_hospital_management/providers/lab_provider.dart';
import 'package:flutter_hospital_management/screens/lab_report_detail_screen.dart';
import 'package:flutter_hospital_management/widgets/common.dart';
import 'package:flutter_hospital_management/widgets/app_drawer.dart';
import 'package:flutter_hospital_management/theme.dart';

class LabDashboardScreen extends ConsumerStatefulWidget {
  const LabDashboardScreen({super.key});

  @override
  ConsumerState<LabDashboardScreen> createState() => _LabDashboardScreenState();
}

class _LabDashboardScreenState extends ConsumerState<LabDashboardScreen> {
  @override
  void initState() {
    super.initState();
    Future.microtask(
        () => ref.read(labDashboardNotifierProvider.notifier).load());
  }

  @override
  Widget build(BuildContext context) {
    final state = ref.watch(labDashboardNotifierProvider);
    final d = state.dashboard;

    return Scaffold(
      appBar: AppBar(
        title: const Text('Lab Dashboard'),
        actions: [
          IconButton(
            icon: const Icon(Icons.refresh),
            onPressed: () =>
                ref.read(labDashboardNotifierProvider.notifier).load(),
          )
        ],
      ),
      drawer: const AppDrawer(),
      body: state.isLoading
          ? const Center(child: CircularProgressIndicator())
          : state.error != null
              ? Center(child: Text(state.error!, style: const TextStyle(color: AppTheme.danger)))
              : d == null
                  ? const EmptyState('No data')
                  : RefreshIndicator(
                      onRefresh: () =>
                          ref.read(labDashboardNotifierProvider.notifier).load(),
                      child: ListView(
                        padding: const EdgeInsets.all(16),
                        children: [
                          Wrap(
                            spacing: 12,
                            runSpacing: 12,
                            children: [
                              _Stat('Total Reports', d.totalReports, AppTheme.primary),
                              _Stat('Normal', d.normalReports, AppTheme.success),
                              _Stat('Abnormal', d.abnormalReports, AppTheme.warning),
                              _Stat('Critical', d.criticalReports, AppTheme.danger),
                              _Stat('Dengue +', d.denguePositive, AppTheme.info),
                              _Stat('Pending Verif.', d.pendingVerification, Colors.purple),
                              _Stat('Ready', d.readyReports, Colors.teal),
                            ],
                          ),
                          if (d.criticalAlerts.isNotEmpty) ...[
                            const SizedBox(height: 24),
                            const SectionTitle('Critical Alerts',
                                icon: Icons.warning_amber),
                            ...d.criticalAlerts.map((a) => Container(
                                  margin: const EdgeInsets.only(bottom: 8),
                                  padding: const EdgeInsets.all(12),
                                  decoration: BoxDecoration(
                                    color: AppTheme.danger.withValues(alpha: 0.08),
                                    borderRadius: BorderRadius.circular(12),
                                    border: Border.all(
                                        color: AppTheme.danger.withValues(alpha: 0.3)),
                                  ),
                                  child: Row(
                                    children: [
                                      const Icon(Icons.error_outline,
                                          color: AppTheme.danger, size: 18),
                                      const SizedBox(width: 8),
                                      Expanded(
                                          child: Text(a,
                                              style: const TextStyle(
                                                  color: AppTheme.danger,
                                                  fontSize: 13))),
                                    ],
                                  ),
                                )),
                          ],
                          if (d.recentReports.isNotEmpty) ...[
                            const SizedBox(height: 24),
                            const SectionTitle('Recent Reports',
                                icon: Icons.history),
                            ...d.recentReports.map((r) => AppCard(
                                  onTap: () => Navigator.push(
                                    context,
                                    MaterialPageRoute(
                                        builder: (_) =>
                                            LabReportDetailScreen(report: r)),
                                  ),
                                  child: Row(
                                    children: [
                                      Expanded(
                                        child: Column(
                                          crossAxisAlignment:
                                              CrossAxisAlignment.start,
                                          children: [
                                            Text(
                                                r.reportNumber ?? '#${r.id}',
                                                style: const TextStyle(
                                                    fontWeight:
                                                        FontWeight.w600)),
                                            const SizedBox(height: 3),
                                            Text(
                                                '${r.testName ?? ''} • ${r.patientName ?? ''}',
                                                style: const TextStyle(
                                                    fontSize: 13,
                                                    color: Colors.grey)),
                                          ],
                                        ),
                                      ),
                                      StatusChip.fromStatus(
                                          r.statusLabel ?? 'N/A'),
                                    ],
                                  ),
                                )),
                          ],
                        ],
                      ),
                    ),
    );
  }
}

class _Stat extends StatelessWidget {
  final String label;
  final int value;
  final Color color;

  const _Stat(this.label, this.value, this.color);

  @override
  Widget build(BuildContext context) => Container(
        width: (MediaQuery.of(context).size.width - 44) / 2,
        padding: const EdgeInsets.all(14),
        decoration: BoxDecoration(
          color: Colors.white,
          borderRadius: BorderRadius.circular(16),
          boxShadow: const [
            BoxShadow(color: Color(0x1A000000), blurRadius: 8, offset: Offset(0, 3))
          ],
        ),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Text(label, style: const TextStyle(fontSize: 12, color: Colors.grey)),
            const SizedBox(height: 6),
            Text('$value',
                style: TextStyle(
                    fontSize: 24,
                    fontWeight: FontWeight.bold,
                    color: color)),
          ],
        ),
      );
}
