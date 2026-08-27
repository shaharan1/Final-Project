import 'package:flutter/material.dart';
import 'package:flutter_hospital_management/widgets/app_drawer.dart';
import 'package:flutter_hospital_management/providers/appointment_provider.dart';
import 'package:flutter_hospital_management/models/appointment.dart';
import 'package:flutter_hospital_management/widgets/common.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';

class AppointmentDetailScreen extends ConsumerStatefulWidget {
  final Appointment appointment;
  const AppointmentDetailScreen({super.key, required this.appointment});

  @override
  ConsumerState<AppointmentDetailScreen> createState() =>
      _AppointmentDetailScreenState();
}

class _AppointmentDetailScreenState
    extends ConsumerState<AppointmentDetailScreen> {
  late Appointment _a;

  @override
  void initState() {
    super.initState();
    _a = widget.appointment;
  }

  bool get _cancelled => (_a.status ?? '').toUpperCase() == 'CANCELLED';

  Future<void> _cancel() async {
    await ref.read(appointmentNotifierProvider.notifier).cancel(_a.id!);
    if (mounted) {
      final updated =
          ref.read(appointmentNotifierProvider).appointments.firstWhere(
                (e) => e.id == _a.id,
                orElse: () => _a,
              );
      setState(() => _a = updated);
    }
  }

  @override
  Widget build(BuildContext context) {
    final a = _a;
    return Scaffold(
      appBar: AppBar(
        title: Text(a.appointmentNumber ?? 'Appointment'),
        actions: [
          if (!_cancelled)
            TextButton.icon(
              onPressed: _cancel,
              icon: const Icon(Icons.cancel, color: Colors.white),
              label: const Text('Cancel',
                  style: TextStyle(color: Colors.white)),
            ),
        ],
      ),
      drawer: const AppDrawer(),
      body: ListView(
        padding: const EdgeInsets.all(16),
        children: [
          Center(
            child: Column(
              children: [
                CircleAvatar(
                  radius: 38,
                  backgroundColor: AppTheme.primary.withValues(alpha: 0.12),
                  child: Text(
                    '${a.tokenNumber ?? a.serialNo ?? '?'}',
                    style: const TextStyle(
                        fontSize: 22,
                        fontWeight: FontWeight.bold,
                        color: AppTheme.primary),
                  ),
                ),
                const SizedBox(height: 10),
                Text(a.patientName ?? 'Unknown',
                    style: const TextStyle(
                        fontSize: 20, fontWeight: FontWeight.bold)),
                const SizedBox(height: 6),
                StatusChip.fromStatus(a.status ?? 'N/A'),
                const SizedBox(height: 4),
                Text('Fee: ৳ ${(a.feeCharged ?? 0).toStringAsFixed(2)}',
                    style: const TextStyle(color: Colors.grey)),
              ],
            ),
          ),
          const SizedBox(height: 20),
          DetailSection('Appointment', [
            DetailRow('Appointment No', a.appointmentNumber,
                icon: Icons.confirmation_number),
            DetailRow('Date', a.appointmentDate, icon: Icons.calendar_today),
            DetailRow('Time', a.appointmentTime, icon: Icons.access_time),
            DetailRow('Problem', a.problemDescription,
                icon: Icons.notes),
            DetailRow('Specialization', a.specialization,
                icon: Icons.medical_services),
            DetailRow('Status', a.status, icon: Icons.info),
          ], icon: Icons.event),
          DetailSection('Doctor', [
            DetailRow('Name', a.doctorName, icon: Icons.person),
            DetailRow('Specialization', a.doctorSpecialization ??
                a.specialization,
                icon: Icons.medical_services),
            DetailRow('Chamber', a.doctorChamber, icon: Icons.meeting_room),
          ], icon: Icons.medical_information),
          DetailSection('Patient', [
            DetailRow('Name', a.patientName, icon: Icons.person),
            DetailRow('Mobile', a.mobileNumber, icon: Icons.phone),
            if (a.registeredPatientId != null)
              DetailRow('Patient ID', '${a.registeredPatientId}',
                  icon: Icons.badge),
          ], icon: Icons.contact_page),
          DetailSection('Payment', [
            DetailRow('Fee Charged',
                '৳ ${(a.feeCharged ?? 0).toStringAsFixed(2)}',
                icon: Icons.payments),
            DetailRow('Method', a.paymentMethod, icon: Icons.payment),
            DetailRow('Transaction ID', a.transactionId,
                icon: Icons.receipt),
          ], icon: Icons.account_balance_wallet),
          const SizedBox(height: 8),
        ],
      ),
    );
  }
}
