import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:flutter_hospital_management/providers/insurance_provider.dart';
import 'package:flutter_hospital_management/widgets/common.dart';
import 'package:flutter_hospital_management/widgets/app_drawer.dart';

class InsuranceClaimListScreen extends ConsumerStatefulWidget {
  const InsuranceClaimListScreen({super.key});

  @override
  ConsumerState<InsuranceClaimListScreen> createState() =>
      _InsuranceClaimListScreenState();
}

class _InsuranceClaimListScreenState
    extends ConsumerState<InsuranceClaimListScreen> {
  @override
  void initState() {
    super.initState();
    Future.microtask(
        () => ref.read(insuranceNotifierProvider.notifier).load());
  }

  String _money(dynamic v) =>
      '৳ ${(v is num ? v.toDouble() : 0).toStringAsFixed(2)}';

  @override
  Widget build(BuildContext context) {
    final state = ref.watch(insuranceNotifierProvider);
    final claims = state.claims;
    return Scaffold(
      appBar: AppBar(title: const Text('Insurance Claims')),
      drawer: const AppDrawer(),
      body: state.isLoading
          ? const Center(child: CircularProgressIndicator())
          : state.error != null
              ? Center(child: Text(state.error!, style: const TextStyle(color: Colors.red)))
              : claims.isEmpty
                  ? const EmptyState('No claims found', icon: Icons.receipt_long)
                  : RefreshIndicator(
                      onRefresh: () =>
                          ref.read(insuranceNotifierProvider.notifier).load(),
                      child: ListView.separated(
                        padding: const EdgeInsets.all(16),
                        itemCount: claims.length,
                        separatorBuilder: (_, _) => const SizedBox(height: 10),
                        itemBuilder: (_, i) {
                          final c = claims[i];
                          return AppCard(
                            child: Column(
                              crossAxisAlignment: CrossAxisAlignment.start,
                              children: [
                                Row(
                                  mainAxisAlignment:
                                      MainAxisAlignment.spaceBetween,
                                  children: [
                                    Text(c.claimNumber ?? 'Claim #${c.id}',
                                        style: const TextStyle(
                                            fontWeight: FontWeight.w600,
                                            fontSize: 15)),
                                    StatusChip.fromStatus(c.claimStatus ?? 'N/A'),
                                  ],
                                ),
                                const SizedBox(height: 6),
                                Text(
                                    '${c.patientName ?? ''}  •  ${c.insuranceCompanyName ?? ''}',
                                    style: const TextStyle(
                                        fontSize: 13, color: Colors.grey)),
                                const SizedBox(height: 4),
                                Text(
                                    'Claim: ${_money(c.claimAmount)}  •  Approved: ${_money(c.approvedAmount)}',
                                    style: const TextStyle(
                                        fontSize: 12, color: Colors.grey)),
                              ],
                            ),
                          );
                        },
                      ),
                    ),
    );
  }
}
