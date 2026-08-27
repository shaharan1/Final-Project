import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:flutter_hospital_management/models/bed.dart';
import 'package:flutter_hospital_management/widgets/app_drawer.dart';
import 'package:flutter_hospital_management/widgets/common.dart';
import 'package:flutter_hospital_management/theme.dart';

class BedDetailScreen extends ConsumerStatefulWidget {
  final Bed item;
  const BedDetailScreen({super.key, required this.item});
  @override
  ConsumerState<BedDetailScreen> createState() => _BedDetailScreenState();
}

class _BedDetailScreenState extends ConsumerState<BedDetailScreen> {
  late Bed _m;

  @override
  void initState() {
    super.initState();
    _m = widget.item;
  }

  @override
  Widget build(BuildContext context) {
    final m = _m;
    return Scaffold(
      appBar: AppBar(title: Text('Bed ${m.bedNumber ?? ''}')),
      drawer: const AppDrawer(),
      body: ListView(
        padding: const EdgeInsets.all(16),
        children: [
          Center(
            child: CircleAvatar(
              radius: 38,
              backgroundColor: AppTheme.primary.withValues(alpha: 0.12),
              child: const Icon(Icons.bed, size: 40, color: AppTheme.primary),
            ),
          ),
          const SizedBox(height: 10),
          Center(
            child: Text('Bed ${m.bedNumber ?? '—'}',
                style: const TextStyle(
                    fontSize: 20, fontWeight: FontWeight.bold)),
          ),
          const SizedBox(height: 20),
          DetailSection('Information', [
            DetailRow('Bed Number', m.bedNumber, icon: Icons.bed),
            DetailRow('Ward', m.wardName, icon: Icons.meeting_room),
            DetailRow('Room Type', m.roomType, icon: Icons.category),
            DetailRow('Status', m.status, icon: Icons.info),
            DetailRow('Daily Cost',
                m.totalDailyCost != null ? '৳ ${m.totalDailyCost!.toStringAsFixed(2)}' : null,
                icon: Icons.attach_money),
            DetailRow('Ward ID', m.wardId?.toString(), icon: Icons.numbers),
          ], icon: Icons.info),
          const SizedBox(height: 8),
        ],
      ),
    );
  }
}
