import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:flutter_hospital_management/providers/clinical_provider.dart';
import 'package:flutter_hospital_management/widgets/common.dart';
import 'package:flutter_hospital_management/widgets/app_drawer.dart';

class DietPlanListScreen extends ConsumerStatefulWidget {
  const DietPlanListScreen({super.key});

  @override
  ConsumerState<DietPlanListScreen> createState() => _DietPlanListScreenState();
}

class _DietPlanListScreenState extends ConsumerState<DietPlanListScreen> {
  @override
  void initState() {
    super.initState();
    Future.microtask(() => ref.read(dietPlanNotifierProvider.notifier).load());
  }

  @override
  Widget build(BuildContext context) {
    final state = ref.watch(dietPlanNotifierProvider);
    return Scaffold(
      appBar: AppBar(title: const Text('Diet Plans')),
      drawer: const AppDrawer(),
      body: state.isLoading
          ? const Center(child: CircularProgressIndicator())
          : state.error != null
              ? Center(child: Text(state.error!, style: const TextStyle(color: Colors.red)))
              : state.plans.isEmpty
                  ? const EmptyState('No diet plans found', icon: Icons.restaurant_menu)
                  : RefreshIndicator(
                      onRefresh: () =>
                          ref.read(dietPlanNotifierProvider.notifier).load(),
                      child: ListView.separated(
                        padding: const EdgeInsets.all(16),
                        itemCount: state.plans.length,
                        separatorBuilder: (_, _) => const SizedBox(height: 10),
                        itemBuilder: (_, i) {
                          final p = state.plans[i];
                          return AppCard(
                            child: Row(
                              children: [
                                CircleAvatar(
                                  backgroundColor: const Color(0xFF0E7C86)
                                      .withValues(alpha: 0.12),
                                  child: const Icon(Icons.restaurant_menu,
                                      color: Color(0xFF0E7C86)),
                                ),
                                const SizedBox(width: 14),
                                Expanded(
                                  child: Column(
                                    crossAxisAlignment:
                                        CrossAxisAlignment.start,
                                    children: [
                                      Text(p.name ?? 'Plan',
                                          style: const TextStyle(
                                              fontWeight: FontWeight.w600,
                                              fontSize: 15)),
                                      const SizedBox(height: 4),
                                      Text(
                                        '${p.dietType ?? ''}  •  ${p.totalCalories ?? 0} kcal  •  ৳ ${p.pricePerDay ?? 0}',
                                        style: const TextStyle(
                                            fontSize: 13, color: Colors.grey),
                                      ),
                                      const SizedBox(height: 6),
                                      StatusChip.fromStatus(
                                          p.active == true ? 'ACTIVE' : 'INACTIVE'),
                                    ],
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
