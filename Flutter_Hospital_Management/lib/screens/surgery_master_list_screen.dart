import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:flutter_hospital_management/providers/clinical_provider.dart';
import 'package:flutter_hospital_management/screens/surgery_master_detail_screen.dart';
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
  String _filter = 'All';

  @override
  void initState() {
    super.initState();
    Future.microtask(() => ref.read(clinicalNotifierProvider.notifier).load());
  }

  String _money(dynamic v) =>
      '৳ ${(v is num ? v.toDouble() : 0).toStringAsFixed(2)}';

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
    final state = ref.watch(clinicalNotifierProvider);
    final masters = state.masters;
    final values = {
      for (final x in masters)
        if (x.categoryName != null && x.categoryName!.isNotEmpty)
          x.categoryName!
    }.toList()
      ..sort();
    final chips = ['All', ...values];
    final filtered = _filter == 'All'
        ? masters
        : masters.where((x) => (x.categoryName ?? '') == _filter).toList();
    return Scaffold(
      appBar: AppBar(title: const Text('Surgery Catalog')),
      drawer: const AppDrawer(),
      body: state.isLoading
          ? const Center(child: CircularProgressIndicator())
          : state.error != null
              ? Center(child: Text(state.error!, style: const TextStyle(color: Colors.red)))
              : masters.isEmpty
                  ? const EmptyState('No surgery catalog found', icon: Icons.category)
                  : Column(
                      children: [
                        _buildChips(chips),
                        Expanded(
                          child: RefreshIndicator(
                      onRefresh: () =>
                          ref.read(clinicalNotifierProvider.notifier).load(),
                      child: ListView.separated(
                        padding: const EdgeInsets.all(16),
                        itemCount: filtered.length,
                        separatorBuilder: (_, _) => const SizedBox(height: 10),
                        itemBuilder: (_, i) {
                          final m = filtered[i];
                          return AppCard(
                            child: Row(
                              children: [
                                GestureDetector(
                                  onTap: () => Navigator.push(
                                      context,
                                      MaterialPageRoute(
                                          builder: (_) =>
                                              SurgeryMasterDetailScreen(
                                                  item: m))),
                                  child: CircleAvatar(
                                    backgroundColor: const Color(0xFF0E7C86)
                                        .withValues(alpha: 0.12),
                                    child: const Icon(Icons.category,
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
                                                SurgeryMasterDetailScreen(
                                                    item: m))),
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
                                              fontSize: 13,
                                              color: Colors.grey),
                                        ),
                                        const SizedBox(height: 6),
                                        StatusChip.fromStatus(
                                            m.active == true
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
                    ),
    );
  }
}
