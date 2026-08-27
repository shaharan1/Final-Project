import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:flutter_hospital_management/models/admission.dart';
import 'package:flutter_hospital_management/widgets/app_drawer.dart';
import 'package:flutter_hospital_management/widgets/common.dart';
import 'package:flutter_hospital_management/theme.dart';

class AdmissionDetailScreen extends ConsumerStatefulWidget {
  final Admission item;
  const AdmissionDetailScreen({super.key, required this.item});
  @override
  ConsumerState<AdmissionDetailScreen> createState() =>
      _AdmissionDetailScreenState();
}

class _AdmissionDetailScreenState extends ConsumerState<AdmissionDetailScreen> {
  late Admission _m;

  @override
  void initState() {
    super.initState();
    _m = widget.item;
  }

  @override
  Widget build(BuildContext context) {
    final m = _m;
    return Scaffold(
      appBar: AppBar(title: Text(m.patientName ?? 'Admission')),
      drawer: const AppDrawer(),
      body: ListView(
        padding: const EdgeInsets.all(16),
        children: [
          Center(
            child: CircleAvatar(
              radius: 38,
              backgroundColor: AppTheme.primary.withValues(alpha: 0.12),
              child: const Icon(Icons.assignment_ind,
                  size: 40, color: AppTheme.primary),
            ),
          ),
          const SizedBox(height: 10),
          Center(
            child: Text(m.patientName ?? '—',
                style: const TextStyle(
                    fontSize: 20, fontWeight: FontWeight.bold)),
          ),
          const SizedBox(height: 4),
          Center(
            child: Text(
              [if (m.patientCode != null) m.patientCode!, if (m.status != null) m.status!]
                  .join('  •  '),
              style: const TextStyle(color: Colors.grey),
            ),
          ),
          const SizedBox(height: 20),
          DetailSection('Patient', [
            DetailRow('Patient Name', m.patientName, icon: Icons.person),
            DetailRow('Patient Code', m.patientCode, icon: Icons.badge),
            DetailRow('Patient ID', m.patientId?.toString(), icon: Icons.numbers),
          ], icon: Icons.person),
          DetailSection('Admission', [
            DetailRow('Admission ID', m.admissionId?.toString(),
                icon: Icons.confirmation_number),
            DetailRow('Status', m.status, icon: Icons.info),
            DetailRow('Admission Date', m.admissionDate, icon: Icons.calendar_today),
            DetailRow('Attending Doctor', m.doctorName, icon: Icons.medical_services),
            DetailRow('Initial Diagnosis', m.initialDiagnosis,
                icon: Icons.medical_information),
          ], icon: Icons.assignment),
          DetailSection('Allocation', [
            DetailRow('Bed ID', m.bedId?.toString(), icon: Icons.bed),
            DetailRow('Assigned Bed', m.assignedBedNumber, icon: Icons.bed),
            DetailRow('Ward', m.wardName, icon: Icons.meeting_room),
          ], icon: Icons.meeting_room),
          const SizedBox(height: 8),
        ],
      ),
    );
  }
}
