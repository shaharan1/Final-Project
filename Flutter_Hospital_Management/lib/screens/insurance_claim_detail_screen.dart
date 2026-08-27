import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:flutter_hospital_management/models/insurance.dart';
import 'package:flutter_hospital_management/widgets/app_drawer.dart';
import 'package:flutter_hospital_management/widgets/common.dart';
import 'package:flutter_hospital_management/theme.dart';

class InsuranceClaimDetailScreen extends ConsumerStatefulWidget {
  final InsuranceClaim item;
  const InsuranceClaimDetailScreen({super.key, required this.item});

  @override
  ConsumerState<InsuranceClaimDetailScreen> createState() =>
      _InsuranceClaimDetailScreenState();
}

class _InsuranceClaimDetailScreenState
    extends ConsumerState<InsuranceClaimDetailScreen> {
  late InsuranceClaim _m;

  String _money(dynamic v) =>
      '৳ ${(v is num ? v.toDouble() : 0).toStringAsFixed(2)}';

  @override
  void initState() {
    super.initState();
    _m = widget.item;
  }

  @override
  Widget build(BuildContext context) {
    final m = _m;
    return Scaffold(
      appBar: AppBar(title: Text(m.claimNumber ?? 'Claim')),
      drawer: const AppDrawer(),
      body: ListView(
        padding: const EdgeInsets.all(16),
        children: [
          Center(
            child: CircleAvatar(
              radius: 38,
              backgroundColor: AppTheme.primary.withValues(alpha: 0.12),
              child: const Icon(Icons.receipt_long,
                  size: 40, color: AppTheme.primary),
            ),
          ),
          const SizedBox(height: 10),
          Center(
            child: Text(m.claimNumber ?? '—',
                style: const TextStyle(
                    fontSize: 20, fontWeight: FontWeight.bold)),
          ),
          const SizedBox(height: 6),
          Center(
            child: StatusChip.fromStatus(m.claimStatus ?? 'N/A'),
          ),
          const SizedBox(height: 20),
          DetailSection('Claim Details', [
            DetailRow('Claim Reference', m.claimReference,
                icon: Icons.confirmation_number),
            DetailRow('Claim Number', m.claimNumber, icon: Icons.receipt_long),
            DetailRow('Patient Name', m.patientName, icon: Icons.person),
            DetailRow('Insurance Company', m.insuranceCompanyName,
                icon: Icons.health_and_safety),
            DetailRow('Policy Number', m.policyNumber, icon: Icons.policy),
            DetailRow('Processed By', m.processedBy, icon: Icons.supervisor_account),
          ], icon: Icons.description),
          const SizedBox(height: 8),
          DetailSection('Amount & Status', [
            DetailRow('Claim Amount', _money(m.claimAmount),
                icon: Icons.attach_money),
            DetailRow('Approved Amount', _money(m.approvedAmount),
                icon: Icons.check_circle),
            DetailRow('Claim Status', m.claimStatus, icon: Icons.flag),
          ], icon: Icons.account_balance_wallet),
          const SizedBox(height: 8),
        ],
      ),
    );
  }
}
