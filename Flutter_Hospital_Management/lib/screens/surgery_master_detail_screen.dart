import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:flutter_hospital_management/models/clinical.dart';
import 'package:flutter_hospital_management/theme.dart';
import 'package:flutter_hospital_management/widgets/app_drawer.dart';
import 'package:flutter_hospital_management/widgets/common.dart';

class SurgeryMasterDetailScreen extends ConsumerStatefulWidget {
  final SurgeryMaster item;
  const SurgeryMasterDetailScreen({super.key, required this.item});

  @override
  ConsumerState<SurgeryMasterDetailScreen> createState() =>
      _SurgeryMasterDetailScreenState();
}

class _SurgeryMasterDetailScreenState
    extends ConsumerState<SurgeryMasterDetailScreen> {
  late SurgeryMaster _m;

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
      appBar: AppBar(title: Text(m.surgeryName ?? 'Surgery')),
      drawer: const AppDrawer(),
      body: ListView(
        padding: const EdgeInsets.all(16),
        children: [
          Center(
            child: CircleAvatar(
              radius: 38,
              backgroundColor: AppTheme.primary.withValues(alpha: 0.12),
              child: const Icon(Icons.category,
                  size: 40, color: AppTheme.primary),
            ),
          ),
          const SizedBox(height: 10),
          Center(
            child: Text(m.surgeryName ?? '—',
                style: const TextStyle(
                    fontSize: 20, fontWeight: FontWeight.bold)),
          ),
          const SizedBox(height: 6),
          Center(child: StatusChip.fromStatus(m.active == true ? 'ACTIVE' : 'INACTIVE')),
          const SizedBox(height: 20),
          DetailSection('Catalog Information', [
            DetailRow('Surgery Name', m.surgeryName, icon: Icons.category),
            DetailRow('Surgery Code', m.surgeryCode, icon: Icons.confirmation_number),
            DetailRow('Category', m.categoryName, icon: Icons.class_),
            DetailRow('Estimated Duration',
                m.estimatedDurationMin != null ? '${m.estimatedDurationMin} min' : null,
                icon: Icons.timer),
            DetailRow('Standard Rate', _money(m.standardRate),
                icon: Icons.attach_money),
          ], icon: Icons.info),
          const SizedBox(height: 8),
        ],
      ),
    );
  }
}
