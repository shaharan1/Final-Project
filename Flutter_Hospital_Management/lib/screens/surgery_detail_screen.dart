import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:flutter_hospital_management/models/clinical.dart';
import 'package:flutter_hospital_management/theme.dart';
import 'package:flutter_hospital_management/widgets/app_drawer.dart';
import 'package:flutter_hospital_management/widgets/common.dart';

class SurgeryDetailScreen extends ConsumerStatefulWidget {
  final Surgery item;
  const SurgeryDetailScreen({super.key, required this.item});

  @override
  ConsumerState<SurgeryDetailScreen> createState() =>
      _SurgeryDetailScreenState();
}

class _SurgeryDetailScreenState extends ConsumerState<SurgeryDetailScreen> {
  late Surgery _m;

  @override
  void initState() {
    super.initState();
    _m = widget.item;
  }

  String _money(dynamic v) =>
      '৳ ${(v is num ? v.toDouble() : 0).toStringAsFixed(2)}';

  @override
  Widget build(BuildContext context) {
    final m = _m;
    return Scaffold(
      appBar: AppBar(title: Text(m.surgeryNumber ?? 'Surgery')),
      drawer: const AppDrawer(),
      body: ListView(
        padding: const EdgeInsets.all(16),
        children: [
          Center(
            child: CircleAvatar(
              radius: 38,
              backgroundColor: AppTheme.primary.withValues(alpha: 0.12),
              child: const Icon(Icons.medical_information,
                  size: 40, color: AppTheme.primary),
            ),
          ),
          const SizedBox(height: 10),
          Center(
            child: Text(m.surgeryNumber ?? '—',
                style: const TextStyle(
                    fontSize: 20, fontWeight: FontWeight.bold)),
          ),
          const SizedBox(height: 6),
          Center(child: StatusChip.fromStatus(m.status ?? 'N/A')),
          const SizedBox(height: 20),
          DetailSection('Surgery Information', [
            DetailRow('Surgery Number', m.surgeryNumber,
                icon: Icons.confirmation_number),
            DetailRow('Surgery Name', m.surgeryName, icon: Icons.medical_services),
            DetailRow('Status', m.status, icon: Icons.flag),
            DetailRow('Priority', m.priority, icon: Icons.priority_high),
            DetailRow('Surgery Date', m.surgeryDate, icon: Icons.calendar_today),
          ], icon: Icons.info),
          DetailSection('Patient & Staff', [
            DetailRow('Patient Name', m.patientName, icon: Icons.person),
            DetailRow('Patient Code', m.patientCode, icon: Icons.badge),
            DetailRow('Surgeon', m.surgeonName, icon: Icons.medical_information),
            DetailRow('Ward', m.wardName, icon: Icons.meeting_room),
            DetailRow('Bed Number', m.bedNumber, icon: Icons.bed),
          ], icon: Icons.people),
          DetailSection('Billing', [
            DetailRow('Total Amount', _money(m.totalAmount),
                icon: Icons.account_balance_wallet),
            DetailRow('Final Payable', _money(m.finalPayable),
                icon: Icons.paid),
          ], icon: Icons.receipt_long),
          const SizedBox(height: 8),
        ],
      ),
    );
  }
}
