import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:flutter_hospital_management/models/test_order.dart';
import 'package:flutter_hospital_management/widgets/app_drawer.dart';
import 'package:flutter_hospital_management/widgets/common.dart';
import 'package:flutter_hospital_management/theme.dart';

class TestOrderDetailScreen extends ConsumerStatefulWidget {
  final TestOrder item;
  const TestOrderDetailScreen({super.key, required this.item});

  @override
  ConsumerState<TestOrderDetailScreen> createState() =>
      _TestOrderDetailScreenState();
}

class _TestOrderDetailScreenState extends ConsumerState<TestOrderDetailScreen> {
  late TestOrder _m;

  @override
  void initState() {
    super.initState();
    _m = widget.item;
  }

  @override
  Widget build(BuildContext context) {
    final m = _m;
    return Scaffold(
      appBar: AppBar(
          title: Text(m.testName ?? m.testCode ?? 'Test Order #${m.id}')),
      drawer: const AppDrawer(),
      body: ListView(
        padding: const EdgeInsets.all(16),
        children: [
          Center(
            child: CircleAvatar(
              radius: 38,
              backgroundColor: AppTheme.primary.withValues(alpha: 0.12),
              child: const Icon(Icons.science_outlined,
                  size: 40, color: AppTheme.primary),
            ),
          ),
          const SizedBox(height: 10),
          Center(
            child: Text(m.testName ?? m.testCode ?? 'Test Order',
                style: const TextStyle(
                    fontSize: 20, fontWeight: FontWeight.bold)),
          ),
          const SizedBox(height: 4),
          Center(
            child: StatusChip.fromStatus(m.orderStatus ?? 'N/A'),
          ),
          const SizedBox(height: 20),
          DetailSection('Order Information', [
            DetailRow('Order #', m.id.toString(), icon: Icons.tag),
            DetailRow('Test Code', m.testCode, icon: Icons.code),
            DetailRow('Test Name', m.testName, icon: Icons.science_outlined),
            DetailRow('Status', m.orderStatus, icon: Icons.info),
            DetailRow('Ordered Date', m.orderedDate, icon: Icons.calendar_today),
          ], icon: Icons.receipt_long),
          const SizedBox(height: 8),
          DetailSection('Test Details', [
            DetailRow('Normal Range', m.normalRange, icon: Icons.straighten),
            DetailRow('Standard Price',
                m.standardPrice != null ? m.standardPrice.toString() : null,
                icon: Icons.attach_money),
            DetailRow('Sample Type', m.sampleType, icon: Icons.biotech),
          ], icon: Icons.checklist),
          const SizedBox(height: 8),
          DetailSection('Patient', [
            DetailRow('Name', m.patientName, icon: Icons.person),
            DetailRow('Patient Code', m.patientCode, icon: Icons.badge),
            DetailRow('Phone', m.patientPhone, icon: Icons.phone),
            DetailRow('Gender', m.patientGender, icon: Icons.wc),
          ], icon: Icons.contact_emergency),
          const SizedBox(height: 8),
          DetailSection('Doctor', [
            DetailRow('Name', m.doctorName, icon: Icons.medical_services),
            DetailRow('Specialization', m.doctorSpecialization,
                icon: Icons.school),
          ], icon: Icons.local_hospital),
          const SizedBox(height: 8),
          DetailSection('Result', [
            DetailRow('Result Value', m.resultValue, icon: Icons.insights),
            DetailRow('Result Notes', m.resultNotes, icon: Icons.notes),
          ], icon: Icons.analytics),
          const SizedBox(height: 8),
        ],
      ),
    );
  }
}
