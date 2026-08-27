import 'package:flutter/material.dart';
import 'package:flutter_hospital_management/core/role_access.dart';
import 'package:flutter_hospital_management/models/appointment.dart';
import 'package:flutter_hospital_management/models/prescription.dart';
import 'package:flutter_hospital_management/providers/auth_provider.dart';
import 'package:flutter_hospital_management/providers/doctor_provider.dart';
import 'package:flutter_hospital_management/providers/prescription_provider.dart';
import 'package:flutter_hospital_management/providers/appointment_provider.dart';
import 'package:flutter_hospital_management/widgets/app_drawer.dart';
import 'package:flutter_hospital_management/widgets/common.dart';
import 'package:flutter_hospital_management/theme.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:flutter_hospital_management/screens/prescription_form_screen.dart';

class DoctorDashboardScreen extends ConsumerStatefulWidget {
  const DoctorDashboardScreen({super.key});

  @override
  ConsumerState<DoctorDashboardScreen> createState() =>
      _DoctorDashboardScreenState();
}

class _DoctorDashboardScreenState
    extends ConsumerState<DoctorDashboardScreen> {
  List<Appointment> _appointments = [];
  bool _loadingAppts = true;

  String get _today {
    final d = DateTime.now();
    return '${d.year}-${d.month.toString().padLeft(2, '0')}-${d.day.toString().padLeft(2, '0')}';
  }

  @override
  void initState() {
    super.initState();
    _init();
  }

  Future<void> _init() async {
    final user = ref.read(authNotifierProvider).user;
    if (user?.userId != null) {
      await ref
          .read(doctorNotifierProvider.notifier)
          .loadMyProfile(user!.userId);
    }
    final doctorId = ref.read(doctorNotifierProvider).myDoctor?.id;
    if (doctorId != null) {
      await ref
          .read(prescriptionNotifierProvider.notifier)
          .loadByDoctor(doctorId);
      try {
        final list = await ref
            .read(appointmentServiceProvider)
            .getByDoctor(doctorId);
        if (mounted) {
          setState(() {
            _appointments = list;
            _loadingAppts = false;
          });
        }
      } catch (_) {
        if (mounted) {
          setState(() => _loadingAppts = false);
        }
      }
    }
  }

  @override
  Widget build(BuildContext context) {
    final doctor = ref.watch(doctorNotifierProvider).myDoctor;
    final rx = ref.watch(prescriptionNotifierProvider);

    final doctorId = doctor?.id;
    final todays = _appointments
        .where((a) => (a.appointmentDate ?? '').startsWith(_today))
        .toList();

    return Scaffold(
      appBar: AppBar(title: const Text('Doctor Dashboard')),
      drawer: const AppDrawer(),
      floatingActionButton: doctorId == null
          ? null
          : FloatingActionButton.extended(
              onPressed: () => Navigator.push(
                context,
                MaterialPageRoute(
                  builder: (_) => PrescriptionFormScreen(doctorId: doctorId!),
                ),
              ).then((_) => _init()),
              icon: const Icon(Icons.add),
              label: const Text('New Prescription'),
            ),
      body: ListView(
        padding: const EdgeInsets.all(16),
        children: [
          Container(
            width: double.infinity,
            decoration: const BoxDecoration(gradient: AppTheme.primaryGradient),
            padding: const EdgeInsets.all(18),
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Text(
                  doctor?.displayName ?? 'Doctor',
                  style: const TextStyle(
                      color: Colors.white,
                      fontSize: 22,
                      fontWeight: FontWeight.bold),
                ),
                const SizedBox(height: 4),
                Text(
                  [
                    if (doctor?.specialization != null) doctor!.specialization!,
                    if (doctor?.departmentName != null)
                      doctor!.departmentName!,
                  ].join('  •  '),
                  style: const TextStyle(color: Colors.white70),
                ),
              ],
            ),
          ),
          const SizedBox(height: 20),
          if (doctorId == null)
            const Center(
                child: Padding(
              padding: EdgeInsets.all(24),
              child: Text('Could not load your doctor profile.',
                  style: TextStyle(color: Colors.grey)),
            ))
          else ...[
            SectionTitle('Today\'s Appointments',
                icon: Icons.calendar_today),
            const SizedBox(height: 12),
            if (_loadingAppts)
              const Center(child: CircularProgressIndicator())
            else if (todays.isEmpty)
              const EmptyState('No appointments today',
                  icon: Icons.event_available)
            else
              ...todays.map((a) => _apptCard(a, doctorId)),
            const SizedBox(height: 24),
            SectionTitle('My Prescriptions', icon: Icons.receipt_long),
            const SizedBox(height: 12),
            if (rx.isLoading)
              const Center(child: CircularProgressIndicator())
            else if (rx.prescriptions.isEmpty)
              const EmptyState('No prescriptions yet',
                  icon: Icons.receipt_long)
            else
              ...rx.prescriptions
                  .take(10)
                  .map((p) => _rxCard(p))
                  .toList(),
            const SizedBox(height: 8),
          ],
        ],
      ),
    );
  }

  Widget _apptCard(Appointment a, int doctorId) => AppCard(
        child: Row(
          children: [
            CircleAvatar(
              backgroundColor: AppTheme.primary.withValues(alpha: 0.12),
              child: Text('${a.tokenNumber ?? a.serialNo ?? '?'}',
                  style: const TextStyle(
                      color: AppTheme.primary,
                      fontWeight: FontWeight.bold)),
            ),
            const SizedBox(width: 14),
            Expanded(
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Text(a.patientName ?? 'Unknown',
                      style: const TextStyle(
                          fontWeight: FontWeight.w600, fontSize: 15)),
                  const SizedBox(height: 4),
                  Text(
                    '${a.appointmentTime ?? ''}  •  ${a.status ?? ''}',
                    style: const TextStyle(fontSize: 13, color: Colors.grey),
                  ),
                ],
              ),
            ),
            TextButton(
              onPressed: () => Navigator.push(
                context,
                MaterialPageRoute(
                  builder: (_) => PrescriptionFormScreen(
                    doctorId: doctorId,
                    patientId: a.registeredPatientId,
                    appointmentId: a.id,
                  ),
                ),
              ).then((_) => _init()),
              child: const Text('Prescribe'),
            ),
          ],
        ),
      );

  Widget _rxCard(PrescriptionResponse p) => AppCard(
        child: Row(
          children: [
            const Icon(Icons.receipt_long, color: AppTheme.primary),
            const SizedBox(width: 12),
            Expanded(
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Text(p.prescriptionNumber ?? '#${p.id}',
                      style: const TextStyle(
                          fontWeight: FontWeight.w600, fontSize: 15)),
                  const SizedBox(height: 4),
                  Text(
                    '${p.patientName ?? ''}  •  ${p.diagnosis ?? ''}',
                    style: const TextStyle(fontSize: 13, color: Colors.grey),
                  ),
                ],
              ),
            ),
            StatusChip.fromStatus(p.dispensed == true ? 'DISPENSED' : 'PENDING'),
          ],
        ),
      );
}
