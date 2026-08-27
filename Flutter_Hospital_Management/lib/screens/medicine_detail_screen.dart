import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:flutter_hospital_management/models/medicine.dart';
import 'package:flutter_hospital_management/widgets/app_drawer.dart';
import 'package:flutter_hospital_management/widgets/common.dart';
import 'package:flutter_hospital_management/theme.dart';

class MedicineDetailScreen extends ConsumerStatefulWidget {
  final Medicine item;
  const MedicineDetailScreen({super.key, required this.item});

  @override
  ConsumerState<MedicineDetailScreen> createState() =>
      _MedicineDetailScreenState();
}

class _MedicineDetailScreenState extends ConsumerState<MedicineDetailScreen> {
  late Medicine _m;

  @override
  void initState() {
    super.initState();
    _m = widget.item;
  }

  @override
  Widget build(BuildContext context) {
    final m = _m;
    return Scaffold(
      appBar: AppBar(title: Text(m.medicineName)),
      drawer: const AppDrawer(),
      body: ListView(
        padding: const EdgeInsets.all(16),
        children: [
          Center(
            child: CircleAvatar(
              radius: 38,
              backgroundColor: AppTheme.primary.withValues(alpha: 0.12),
              child: const Icon(Icons.medication, size: 40, color: AppTheme.primary),
            ),
          ),
          const SizedBox(height: 10),
          Center(
            child: Text(m.medicineName,
                style: const TextStyle(fontSize: 20, fontWeight: FontWeight.bold)),
          ),
          const SizedBox(height: 20),
          DetailSection('Information', [
            DetailRow('Medicine Name', m.medicineName, icon: Icons.medication),
            DetailRow('Generic Name', m.genericName, icon: Icons.biotech),
            DetailRow('Dosage', m.dosage, icon: Icons.science),
            DetailRow('Generic ID', m.genericId?.toString(), icon: Icons.tag),
            DetailRow('Prescription ID', m.prescriptionId?.toString(),
                icon: Icons.receipt_long),
            DetailRow('ID', m.id?.toString(), icon: Icons.fingerprint),
          ], icon: Icons.info),
          const SizedBox(height: 8),
        ],
      ),
    );
  }
}
