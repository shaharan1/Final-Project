import 'package:flutter/material.dart';
import 'package:flutter_hospital_management/widgets/app_drawer.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:flutter_hospital_management/providers/lab_provider.dart';
import 'package:flutter_hospital_management/screens/lab_report_detail_screen.dart';
import 'package:flutter_hospital_management/widgets/common.dart';

class LabReportListScreen extends ConsumerStatefulWidget {
  const LabReportListScreen({super.key});

  @override
  ConsumerState<LabReportListScreen> createState() =>
      _LabReportListScreenState();
}

class _LabReportListScreenState extends ConsumerState<LabReportListScreen> {
  @override
  void initState() {
    super.initState();
    Future.microtask(
        () => ref.read(labReportNotifierProvider.notifier).load());
  }

  @override
  Widget build(BuildContext context) {
    final state = ref.watch(labReportNotifierProvider);
    return Scaffold(
      appBar: AppBar(title: const Text('Lab Reports'),
      drawer: const AppDrawer(),
      body: state.isLoading
          ? const Center(child: CircularProgressIndicator())
          : state.error != null
              ? Center(child: Text(state.error!, style: const TextStyle(color: Colors.red)))
              : state.reports.isEmpty
                  ? const EmptyState('No lab reports')
                  : RefreshIndicator(
                      onRefresh: () =>
                          ref.read(labReportNotifierProvider.notifier).load(),
                      child: ListView.separated(
                        padding: const EdgeInsets.all(16),
                        itemCount: state.reports.length,
                        separatorBuilder: (_, _) => const SizedBox(height: 10),
                        itemBuilder: (_, i) {
                          final r = state.reports[i];
                          return AppCard(
                            onTap: () => Navigator.push(
                              context,
                              MaterialPageRoute(
                                  builder: (_) =>
                                      LabReportDetailScreen(report: r)),
                            ),
                            child: Row(
                              children: [
                                CircleAvatar(
                                  backgroundColor:
                                      const Color(0xFF0E7C86).withValues(alpha: 0.12),
                                  child: const Icon(Icons.science,
                                      color: Color(0xFF0E7C86)),
                                ),
                                const SizedBox(width: 14),
                                Expanded(
                                  child: Column(
                                    crossAxisAlignment:
                                        CrossAxisAlignment.start,
                                    children: [
                                      Text(r.reportNumber ?? 'Report #${r.id}',
                                          style: const TextStyle(
                                              fontWeight: FontWeight.w600)),
                                      const SizedBox(height: 4),
                                      Text(
                                          '${r.testName ?? ''}  •  ${r.patientName ?? ''}',
                                          style: const TextStyle(
                                              fontSize: 13,
                                              color: Colors.grey)),
                                    ],
                                  ),
                                ),
                                StatusChip.fromStatus(r.statusLabel ?? r.reportStatus ?? 'N/A'),
                              ],
                            ),
                          );
                        },
                      ),
                    ),
    );
  }
}
