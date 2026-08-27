import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:flutter_hospital_management/models/clinical.dart';
import 'package:flutter_hospital_management/theme.dart';
import 'package:flutter_hospital_management/widgets/app_drawer.dart';
import 'package:flutter_hospital_management/widgets/common.dart';

class DietPlanDetailScreen extends ConsumerStatefulWidget {
  final DietPlan item;
  const DietPlanDetailScreen({super.key, required this.item});

  @override
  ConsumerState<DietPlanDetailScreen> createState() =>
      _DietPlanDetailScreenState();
}

class _DietPlanDetailScreenState extends ConsumerState<DietPlanDetailScreen> {
  late DietPlan _m;

  @override
  void initState() {
    super.initState();
    _m = widget.item;
  }

  @override
  Widget build(BuildContext context) {
    final m = _m;
    return Scaffold(
      appBar: AppBar(title: Text(m.name ?? 'Diet Plan')),
      drawer: const AppDrawer(),
      body: ListView(
        padding: const EdgeInsets.all(16),
        children: [
          Center(
            child: CircleAvatar(
              radius: 38,
              backgroundColor: AppTheme.primary.withValues(alpha: 0.12),
              child: const Icon(Icons.restaurant_menu,
                  size: 40, color: AppTheme.primary),
            ),
          ),
          const SizedBox(height: 10),
          Center(
            child: Text(m.name ?? '—',
                style: const TextStyle(
                    fontSize: 20, fontWeight: FontWeight.bold)),
          ),
          const SizedBox(height: 6),
          Center(child: StatusChip.fromStatus(m.active == true ? 'ACTIVE' : 'INACTIVE')),
          const SizedBox(height: 20),
          DetailSection('Plan Information', [
            DetailRow('Plan Name', m.name, icon: Icons.restaurant_menu),
            DetailRow('Diet Type', m.dietType, icon: Icons.category),
            DetailRow('Description', m.description, icon: Icons.notes),
            DetailRow('Total Calories', m.totalCalories?.toStringAsFixed(0),
                icon: Icons.local_fire_department),
            DetailRow('Protein', m.protein?.toStringAsFixed(1),
                icon: Icons.fitness_center),
            DetailRow('Price Per Day', m.pricePerDay != null ? '৳ ${m.pricePerDay!.toStringAsFixed(2)}' : null,
                icon: Icons.attach_money),
          ], icon: Icons.info),
          const SizedBox(height: 8),
        ],
      ),
    );
  }
}
