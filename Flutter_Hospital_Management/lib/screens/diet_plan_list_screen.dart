import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:flutter_hospital_management/providers/clinical_provider.dart';
import 'package:flutter_hospital_management/screens/diet_plan_detail_screen.dart';
import 'package:flutter_hospital_management/widgets/common.dart';
import 'package:flutter_hospital_management/widgets/app_drawer.dart';

class DietPlanListScreen extends ConsumerStatefulWidget {
  const DietPlanListScreen({super.key});

  @override
  ConsumerState<DietPlanListScreen> createState() => _DietPlanListScreenState();
}

class _DietPlanListScreenState extends ConsumerState<DietPlanListScreen> {
  String _filter = 'All';

  @override
  void initState() {
    super.initState();
    Future.microtask(() => ref.read(dietPlanNotifierProvider.notifier).load());
  }

  Widget _buildChips(List<String> chips) => SingleChildScrollView(
        scrollDirection: Axis.horizontal,
        padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 4),
        child: Row(
          children: chips
              .map((c) => Padding(
                    padding: const EdgeInsets.only(right: 8),
                    child: ChoiceChip(
                      label: Text(c),
                      selected: _filter == c,
                      onSelected: (_) => setState(() => _filter = c),
                    ),
                  ))
              .toList(),
        ),
      );

  @override
  Widget build(BuildContext context) {
    final state = ref.watch(dietPlanNotifierProvider);
    final values = {
      for (final x in state.plans)
        if (x.dietType != null && x.dietType!.isNotEmpty) x.dietType!
    }.toList()
      ..sort();
    final chips = ['All', ...values];
    final filtered = _filter == 'All'
        ? state.plans
        : state.plans.where((x) => (x.dietType ?? '') == _filter).toList();
    return Scaffold(
      appBar: AppBar(title: const Text('Diet Plans')),
      drawer: const AppDrawer(),
      body: state.isLoading
          ? const Center(child: CircularProgressIndicator())
          : state.error != null
              ? Center(child: Text(state.error!, style: const TextStyle(color: Colors.red)))
              : state.plans.isEmpty
                  ? const EmptyState('No diet plans found', icon: Icons.restaurant_menu)
                  : Column(
                      children: [
                        _buildChips(chips),
                        Expanded(
                          child: RefreshIndicator(
                      onRefresh: () =>
                          ref.read(dietPlanNotifierProvider.notifier).load(),
                      child: ListView.separated(
                        padding: const EdgeInsets.all(16),
                        itemCount: filtered.length,
                        separatorBuilder: (_, _) => const SizedBox(height: 10),
                        itemBuilder: (_, i) {
                          final p = filtered[i];
                          return AppCard(
                            child: Row(
                              children: [
                                GestureDetector(
                                  onTap: () => Navigator.push(
                                      context,
                                      MaterialPageRoute(
                                          builder: (_) =>
                                              DietPlanDetailScreen(item: p))),
                                  child: CircleAvatar(
                                    backgroundColor: const Color(0xFF0E7C86)
                                        .withValues(alpha: 0.12),
                                    child: const Icon(Icons.restaurant_menu,
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
                                                DietPlanDetailScreen(item: p))),
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
                                              fontSize: 13,
                                              color: Colors.grey),
                                        ),
                                        const SizedBox(height: 6),
                                        StatusChip.fromStatus(
                                            p.active == true
                                                ? 'ACTIVE'
                                                : 'INACTIVE'),
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
                        ),
    );
  }
}
