import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:flutter_hospital_management/core/constants/app_constants.dart';
import 'package:flutter_hospital_management/core/pdf_launcher.dart';
import 'package:flutter_hospital_management/models/prescription.dart';
import 'package:flutter_hospital_management/providers/prescription_provider.dart';
import 'package:flutter_hospital_management/services/prescription_service.dart';
import 'package:flutter_hospital_management/widgets/app_drawer.dart';
import 'package:flutter_hospital_management/widgets/common.dart';
import 'package:flutter_hospital_management/theme.dart';

class PrescriptionDetailScreen extends ConsumerStatefulWidget {
  final int id;
  const PrescriptionDetailScreen({super.key, required this.id});

  @override
  ConsumerState<PrescriptionDetailScreen> createState() =>
      _PrescriptionDetailScreenState();
}

class _PrescriptionDetailScreenState
    extends ConsumerState<PrescriptionDetailScreen> {
  PrescriptionResponse? _data;
  bool _loading = true;
  String? _error;

  @override
  void initState() {
    super.initState();
    _load();
  }

  Future<void> _load() async {
    try {
      final data = await ref
          .read(prescriptionServiceProvider)
          .getById(widget.id);
      if (mounted) setState(() => _data = data);
    } catch (e) {
      if (mounted) setState(() => _error = e.toString());
    } finally {
      if (mounted) setState(() => _loading = false);
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Prescription'),
        actions: [
          if (_data?.id != null)
            IconButton(
              icon: const Icon(Icons.picture_as_pdf),
              tooltip: 'Download PDF',
              onPressed: () => openPdfInBrowser(
                  '${AppConstants.baseUrl}/prescriptions/${_data!.id}/pdf'),
            ),
        ],
      ),
      drawer: const AppDrawer(),
      body: _loading
          ? const Center(child: CircularProgressIndicator())
          : _error != null
              ? Padding(
                  padding: const EdgeInsets.all(24),
                  child: Text(_error!,
                      style: const TextStyle(color: AppTheme.danger)),
                )
              : _data == null
                  ? const Center(child: Text('Not found'))
                  : ListView(
                      padding: const EdgeInsets.all(16),
                      children: [
                        GradientHeader(
                          _data!.patientName ?? 'Patient',
                          subtitle:
                              '${_data!.prescriptionNumber ?? ''}  •  ${_data!.createdDate ?? ''}',
                        ),
                        const SizedBox(height: 16),
                        AppCard(
                          child: Column(
                            crossAxisAlignment: CrossAxisAlignment.start,
                            children: [
                              _row('Doctor', _data!.doctorName),
                              _row('Diagnosis', _data!.diagnosis),
                              _row('Chief Complaints', _data!.chiefComplaints),
                              _row('Symptoms', _data!.symptoms),
                              _row('Notes', _data!.notes),
                              _row('Next Follow-up', _data!.nextFollowUpDate),
                              _row('Dispensed',
                                  _data!.dispensed == true ? 'Yes' : 'No'),
                            ],
                          ),
                        ),
                        const SizedBox(height: 16),
                        const SectionTitle('Medicines', icon: Icons.medication),
                        const SizedBox(height: 10),
                        AppCard(
                          child: _data!.prescriptionItems.isEmpty
                              ? const Padding(
                                  padding: EdgeInsets.all(12),
                                  child: Text('No medicines listed.',
                                      style: TextStyle(color: Colors.grey)),
                                )
                              : Table(
                                  columnWidths: const {
                                    0: FlexColumnWidth(2),
                                    1: FlexColumnWidth(1.2),
                                    2: FlexColumnWidth(1.2),
                                    3: FlexColumnWidth(1.5),
                                  },
                                  children: [
                                    const TableRow(
                                      decoration: BoxDecoration(
                                          border: Border(
                                              bottom: BorderSide(
                                                  color: Colors.black12))),
                                      children: [
                                        Padding(
                                          padding: EdgeInsets.all(8),
                                          child: Text('Medicine',
                                              style: TextStyle(
                                                  fontWeight: FontWeight.bold)),
                                        ),
                                        Padding(
                                          padding: EdgeInsets.all(8),
                                          child: Text('Dosage',
                                              style: TextStyle(
                                                  fontWeight: FontWeight.bold)),
                                        ),
                                        Padding(
                                          padding: EdgeInsets.all(8),
                                          child: Text('Duration',
                                              style: TextStyle(
                                                  fontWeight: FontWeight.bold)),
                                        ),
                                        Padding(
                                          padding: EdgeInsets.all(8),
                                          child: Text('Instruction',
                                              style: TextStyle(
                                                  fontWeight: FontWeight.bold)),
                                        ),
                                      ],
                                    ),
                                    for (final it in _data!.prescriptionItems)
                                      TableRow(
                                        children: [
                                          Padding(
                                            padding: EdgeInsets.all(8),
                                            child: Text(it.medicineName ??
                                                'Medicine'),
                                          ),
                                          Padding(
                                            padding: EdgeInsets.all(8),
                                            child: Text(it.dosage ?? '-'),
                                          ),
                                          Padding(
                                            padding: EdgeInsets.all(8),
                                            child: Text(it.duration ?? '-'),
                                          ),
                                          Padding(
                                            padding: EdgeInsets.all(8),
                                            child: Text(it.instruction ?? '-'),
                                          ),
                                        ],
                                      ),
                                  ],
                                ),
                        ),
                      ],
                    ),
    );
  }

  Widget _row(String label, String? value) => Padding(
        padding: const EdgeInsets.symmetric(vertical: 6),
        child: Row(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            SizedBox(
              width: 130,
              child: Text(label,
                  style: const TextStyle(
                      color: Colors.grey, fontWeight: FontWeight.w600)),
            ),
            Expanded(
                child: Text(value ?? '-',
                    style: const TextStyle(fontWeight: FontWeight.w500))),
          ],
        ),
      );
}
