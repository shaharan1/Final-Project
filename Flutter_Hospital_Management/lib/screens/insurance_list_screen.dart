import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:flutter_hospital_management/providers/insurance_provider.dart';
import 'package:flutter_hospital_management/widgets/common.dart';
import 'package:flutter_hospital_management/widgets/app_drawer.dart';
import 'package:flutter_hospital_management/screens/insurance_detail_screen.dart';

class InsuranceListScreen extends ConsumerStatefulWidget {
  const InsuranceListScreen({super.key});

  @override
  ConsumerState<InsuranceListScreen> createState() =>
      _InsuranceListScreenState();
}

class _InsuranceListScreenState extends ConsumerState<InsuranceListScreen> {
  @override
  void initState() {
    super.initState();
    Future.microtask(
        () => ref.read(insuranceNotifierProvider.notifier).load());
  }

  @override
  Widget build(BuildContext context) {
    final state = ref.watch(insuranceNotifierProvider);
    return Scaffold(
      appBar: AppBar(title: const Text('Insurance')),
      drawer: const AppDrawer(),
      body: state.isLoading
          ? const Center(child: CircularProgressIndicator())
          : state.error != null
              ? Center(child: Text(state.error!, style: const TextStyle(color: Colors.red)))
              : state.insurances.isEmpty
                  ? const EmptyState('No insurers found', icon: Icons.health_and_safety)
                  : RefreshIndicator(
                      onRefresh: () =>
                          ref.read(insuranceNotifierProvider.notifier).load(),
                      child: ListView.separated(
                        padding: const EdgeInsets.all(16),
                        itemCount: state.insurances.length,
                        separatorBuilder: (_, _) => const SizedBox(height: 10),
                        itemBuilder: (_, i) {
                          final ins = state.insurances[i];
                          return AppCard(
                            child: Row(
                              children: [
                                GestureDetector(
                                  onTap: () => Navigator.push(
                                      context,
                                      MaterialPageRoute(
                                          builder: (_) =>
                                              InsuranceDetailScreen(item: ins))),
                                  child: CircleAvatar(
                                    backgroundColor: const Color(0xFF0E7C86)
                                        .withValues(alpha: 0.12),
                                    child: const Icon(Icons.health_and_safety,
                                        color: Color(0xFF0E7C86)),
                                  ),
                                ),
                                const SizedBox(width: 14),
                                Expanded(
                                  child: GestureDetector(
                                    onTap: () => Navigator.push(
                                        context,
                                        MaterialPageRoute(
                                            builder: (_) =>
                                                InsuranceDetailScreen(item: ins))),
                                    child: Column(
                                    crossAxisAlignment:
                                        CrossAxisAlignment.start,
                                    children: [
                                      Text(ins.companyName ?? 'Insurer',
                                          style: const TextStyle(
                                              fontWeight: FontWeight.w600,
                                              fontSize: 15)),
                                      const SizedBox(height: 4),
                                      Text(
                                        '${ins.contactPerson ?? ''}  •  ${ins.phone ?? ''}',
                                        style: const TextStyle(
                                            fontSize: 13, color: Colors.grey),
                                      ),
                                      const SizedBox(height: 4),
                                      Text(
                                          'Coverage: ${(ins.coveragePercentage ?? 0)}%  (max ৳ ${ins.maxCoverage ?? 0})',
                                          style: const TextStyle(
                                              fontSize: 12, color: Colors.grey)),
                                      const SizedBox(height: 6),
                                      StatusChip.fromStatus(
                                          ins.active == true ? 'ACTIVE' : 'INACTIVE'),
                                    ],
                                  ),
                                  ),
                                ),
                              ],
                            ),
                          );
                        },
                      ),
                    ),
    );
  }
}
