import 'package:flutter/material.dart';
import 'package:flutter_hospital_management/widgets/app_drawer.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:flutter_hospital_management/models/lab_report.dart';
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
  String _filter = 'All';

  @override
  void initState() {
    super.initState();
    Future.microtask(
        () => ref.read(labReportNotifierProvider.notifier).load());
  }

  @override
  Widget build(BuildContext context) {
    final state = ref.watch(labReportNotifierProvider);
    final values = {
      for (final x in state.reports)
        if ((x.statusLabel ?? x.reportStatus) != null &&
            (x.statusLabel ?? x.reportStatus)!.isNotEmpty)
          (x.statusLabel ?? x.reportStatus)!
    }.toList()
      ..sort();
    final chips = ['All', ...values];
    final filtered = _filter == 'All'
        ? state.reports
        : state.reports
            .where((x) => _safeStatus(x) == _filter)
            .toList();
    return Scaffold(
      appBar: AppBar(title: const Text('Lab Reports')),
      drawer: const AppDrawer(),
      body: state.isLoading
          ? const Center(child: CircularProgressIndicator())
          : state.error != null
              ? Center(child: Text(state.error!, style: const TextStyle(color: Colors.red)))
              : state.reports.isEmpty
                  ? const EmptyState('No lab reports')
                  : Column(
                      children: [
                        _buildChips(chips),
                        Expanded(
                          child: RefreshIndicator(
                            onRefresh: () => ref
                                .read(labReportNotifierProvider.notifier)
                                .load(),
                            child: ListView.separated(
                              padding: const EdgeInsets.all(16),
                              itemCount: filtered.length,
                              separatorBuilder: (_, _) =>
                                  const SizedBox(height: 10),
                              itemBuilder: (_, i) {
                                final r = filtered[i];
                                return AppCard(
                                  child: Row(
                                    children: [
                                      GestureDetector(
                                        onTap: () => Navigator.push(
                                          context,
                                          MaterialPageRoute(
                                              builder: (_) =>
                                                  LabReportDetailScreen(
                                                      report: r)),
                                        ),
                                        child: CircleAvatar(
                                          backgroundColor: const Color(
                                                  0xFF0E7C86)
                                              .withValues(alpha: 0.12),
                                          child: const Icon(Icons.science,
                                              color: Color(0xFF0E7C86)),
                                        ),
                                      ),
                                      const SizedBox(width: 14),
                                      Expanded(
                                        child: GestureDetector(
                                          onTap: () => Navigator.push(
                                            context,
                                            MaterialPageRoute(
                                                builder: (_) =>
                                                    LabReportDetailScreen(
                                                        report: r)),
                                          ),
                                          child: Column(
                                            crossAxisAlignment:
                                                CrossAxisAlignment.start,
                                            children: [
                                              Text(
                                                  r.reportNumber ??
                                                      'Report #${r.id}',
                                                  style: const TextStyle(
                                                      fontWeight:
                                                          FontWeight.w600)),
                                              const SizedBox(height: 4),
                                              Text(
                                                  '${r.testName ?? ''}  •  ${r.patientName ?? ''}',
                                                  style: const TextStyle(
                                                      fontSize: 13,
                                                      color: Colors.grey)),
                                            ],
                                          ),
                                        ),
                                      ),
                                      StatusChip.fromStatus(
                                          r.statusLabel ??
                                              r.reportStatus ??
                                              'N/A'),
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

  String _safeStatus(LabReport r) =>
      (r.statusLabel ?? r.reportStatus) ?? 'N/A';

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
}
