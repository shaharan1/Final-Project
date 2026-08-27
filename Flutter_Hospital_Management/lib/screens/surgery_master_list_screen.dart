import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:flutter_hospital_management/providers/clinical_provider.dart';
import 'package:flutter_hospital_management/widgets/common.dart';
import 'package:flutter_hospital_management/widgets/app_drawer.dart';

class SurgeryMasterListScreen extends ConsumerStatefulWidget {
  const SurgeryMasterListScreen({super.key});

  @override
  ConsumerState<SurgeryMasterListScreen> createState() =>
      _SurgeryMasterListScreenState();
}

class _SurgeryMasterListScreenState
    extends ConsumerState<SurgeryMasterListScreen> {
  @override
  void initState() {
    super.initState();
    Future.microtask(() => ref.read(clinicalNotifierProvider.notifier).load());
  }

  String _money(dynamic v) =>
      '৳ ${(v is num ? v.toDouble() : 0).toStringAsFixed(2)}';

  @override
  Widget build(BuildContext context) {
    final state = ref.watch(clinicalNotifierProvider);
    final masters = state.masters;
    return Scaffold(
      appBar: AppBar(title: const Text('Surgery Catalog')),
      drawer: const AppDrawer(),
      body: state.isLoading
          ? const Center(child: CircularProgressIndicator())
          : state.error != null
              ? Center(child: Text(state.error!, style: const TextStyle(color: Colors.red)))
              : masters.isEmpty
                  ? const EmptyState('No surgery catalog found', icon: Icons.category)
                  : RefreshIndicator(
                      onRefresh: () =>
                          ref.read(clinicalNotifierProvider.notifier).load(),
                      child: ListView.separated(
                        padding: const EdgeInsets.all(16),
                        itemCount: masters.length,
                        separatorBuilder: (_, _) => const SizedBox(height: 10),
                        itemBuilder: (_, i) {
                          final m = masters[i];
                          return AppCard(
                            child: Row(
                              children: [
                                CircleAvatar(
                                  backgroundColor: const Color(0xFF0E7C86)
                                      .withValues(alpha: 0.12),
                                  child: const Icon(Icons.category,
                                      color: Color(0xFF0E7C86)),
                                ),
                                const SizedBox(width: 14),
                                Expanded(
                                  child: Column(
                                    crossAxisAlignment:
                                        CrossAxisAlignment.start,
                                    children: [
                                      Text(m.surgeryName ?? 'Surgery',
                                          style: const TextStyle(
                                              fontWeight: FontWeight.w600,
                                              fontSize: 15)),
                                      const SizedBox(height: 4),
                                      Text(
                                        '${m.surgeryCode ?? ''}  •  ${m.categoryName ?? ''}  •  ${_money(m.standardRate)}',
                                        style: const TextStyle(
                                            fontSize: 13, color: Colors.grey),
                                      ),
                                      const SizedBox(height: 6),
                                      StatusChip.fromStatus(
                                          m.active == true ? 'ACTIVE' : 'INACTIVE'),
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
