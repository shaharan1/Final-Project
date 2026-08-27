import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:flutter_hospital_management/models/insurance.dart';
import 'package:flutter_hospital_management/widgets/app_drawer.dart';
import 'package:flutter_hospital_management/widgets/common.dart';
import 'package:flutter_hospital_management/theme.dart';

class InsuranceDetailScreen extends ConsumerStatefulWidget {
  final Insurance item;
  const InsuranceDetailScreen({super.key, required this.item});

  @override
  ConsumerState<InsuranceDetailScreen> createState() =>
      _InsuranceDetailScreenState();
}

class _InsuranceDetailScreenState extends ConsumerState<InsuranceDetailScreen> {
  late Insurance _m;

  @override
  void initState() {
    super.initState();
    _m = widget.item;
  }

  @override
  Widget build(BuildContext context) {
    final m = _m;
    return Scaffold(
      appBar: AppBar(title: Text(m.companyName ?? 'Insurance')),
      drawer: const AppDrawer(),
      body: ListView(
        padding: const EdgeInsets.all(16),
        children: [
          Center(
            child: CircleAvatar(
              radius: 38,
              backgroundColor: AppTheme.primary.withValues(alpha: 0.12),
              child: const Icon(Icons.health_and_safety,
                  size: 40, color: AppTheme.primary),
            ),
          ),
          const SizedBox(height: 10),
          Center(
            child: Text(m.companyName ?? '—',
                style: const TextStyle(
                    fontSize: 20, fontWeight: FontWeight.bold)),
          ),
          const SizedBox(height: 6),
          Center(
            child: StatusChip.fromStatus(m.active == true ? 'ACTIVE' : 'INACTIVE'),
          ),
          const SizedBox(height: 20),
          DetailSection('Policy & Coverage', [
            DetailRow('Company Name', m.companyName,
                icon: Icons.health_and_safety),
            DetailRow('Coverage Percentage',
                m.coveragePercentage != null ? '${m.coveragePercentage}%' : null,
                icon: Icons.percent),
            DetailRow('Max Coverage',
                m.maxCoverage != null ? '৳ ${m.maxCoverage}' : null,
                icon: Icons.savings),
          ], icon: Icons.policy),
          const SizedBox(height: 8),
          DetailSection('Contact', [
            DetailRow('Contact Person', m.contactPerson,
                icon: Icons.person),
            DetailRow('Phone', m.phone, icon: Icons.phone),
            DetailRow('Email', m.email, icon: Icons.email),
          ], icon: Icons.contact_phone),
          const SizedBox(height: 8),
        ],
      ),
    );
  }
}
