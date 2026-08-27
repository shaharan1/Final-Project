import 'package:flutter/material.dart';
import 'package:flutter_hospital_management/models/lab_report.dart';
import 'package:flutter_hospital_management/widgets/common.dart';

class LabReportDetailScreen extends StatelessWidget {
  final LabReport report;

  const LabReportDetailScreen({super.key, required this.report});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text(report.reportNumber ?? 'Report #${report.id}')),
      body: ListView(
        padding: const EdgeInsets.all(16),
        children: [
          AppCard(
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Row(
                  mainAxisAlignment: MainAxisAlignment.spaceBetween,
                  children: [
                    Text(report.testName ?? 'Test',
                        style: const TextStyle(
                            fontWeight: FontWeight.w700, fontSize: 16)),
                    StatusChip.fromStatus(
                        report.statusLabel ?? report.reportStatus ?? 'N/A'),
                  ],
                ),
                const SizedBox(height: 8),
                _row('Patient', report.patientName ?? '-'),
                _row('Code', report.patientCode ?? '-'),
                _row('Doctor', report.doctorName ?? '-'),
                _row('Specialist', report.specialistName ?? '-'),
              ],
            ),
          ),
          const SizedBox(height: 16),
          const SectionTitle('Results', icon: Icons.checklist),
          ...report.results.map((res) => AppCard(
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Row(
                      mainAxisAlignment: MainAxisAlignment.spaceBetween,
                      children: [
                        Expanded(
                            child: Text(res.parameterName ?? '',
                                style: const TextStyle(
                                    fontWeight: FontWeight.w600))),
                        StatusChip.fromStatus(res.statusLabel ?? '-'),
                      ],
                    ),
                    const SizedBox(height: 4),
                    _row('Value', '${res.resultValue ?? '-'} ${res.unit ?? ''}'),
                    _row('Reference', res.referenceRangeDisplay ?? '-'),
                    if (res.interpretation != null)
                      _row('Note', res.interpretation!),
                  ],
                ),
              )),
          if (report.finalImpression != null) ...[
            const SizedBox(height: 16),
            AppCard(
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  const Text('Impression',
                      style: TextStyle(fontWeight: FontWeight.w600)),
                  const SizedBox(height: 6),
                  Text(report.finalImpression!),
                  if (report.recommendation != null) ...[
                    const SizedBox(height: 8),
                    const Text('Recommendation',
                        style: TextStyle(fontWeight: FontWeight.w600)),
                    const SizedBox(height: 6),
                    Text(report.recommendation!),
                  ],
                ],
              ),
            ),
          ],
        ],
      ),
    );
  }

  Widget _row(String label, String value) => Padding(
        padding: const EdgeInsets.symmetric(vertical: 3),
        child: Row(
          mainAxisAlignment: MainAxisAlignment.spaceBetween,
          children: [
            Text(label, style: const TextStyle(color: Colors.grey)),
            Expanded(
              child: Text(value,
                  textAlign: TextAlign.end,
                  style: const TextStyle(fontWeight: FontWeight.w500)),
            ),
          ],
        ),
      );
}
