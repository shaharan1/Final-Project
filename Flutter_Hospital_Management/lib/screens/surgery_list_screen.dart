import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:flutter_hospital_management/providers/clinical_provider.dart';
import 'package:flutter_hospital_management/screens/surgery_detail_screen.dart';
import 'package:flutter_hospital_management/widgets/common.dart';
import 'package:flutter_hospital_management/widgets/app_drawer.dart';

class SurgeryListScreen extends ConsumerStatefulWidget {
  const SurgeryListScreen({super.key});

  @override
  ConsumerState<SurgeryListScreen> createState() => _SurgeryListScreenState();
}

class _SurgeryListScreenState extends ConsumerState<SurgeryListScreen> {
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
    final values = {
      for (final x in state.surgeries)
        if (x.status != null && x.status!.isNotEmpty) x.status!
    }.toList()
      ..sort();
    final chips = ['All', ...values];
    final filtered = _filter == 'All'
        ? state.surgeries
        : state.surgeries.where((x) => (x.status ?? '') == _filter).toList();
    return Scaffold(
      appBar: AppBar(title: const Text('Surgeries')),
      drawer: const AppDrawer(),
      body: state.isLoading
          ? const Center(child: CircularProgressIndicator())
          : state.error != null
              ? Center(child: Text(state.error!, style: const TextStyle(color: Colors.red)))
              : state.surgeries.isEmpty
                  ? const EmptyState('No surgeries found', icon: Icons.medical_information)
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
                          final s = filtered[i];
                          return AppCard(
                            child: GestureDetector(
                              onTap: () => Navigator.push(
                                  context,
                                  MaterialPageRoute(
                                      builder: (_) =>
                                          SurgeryDetailScreen(item: s))),
                              child: Column(
                              crossAxisAlignment: CrossAxisAlignment.start,
                              children: [
                                Row(
                                  mainAxisAlignment:
                                      MainAxisAlignment.spaceBetween,
                                  children: [
                                    Expanded(
                                      child: Text(
                                          s.surgeryNumber ?? 'Surgery #${s.id}',
                                          style: const TextStyle(
                                              fontWeight: FontWeight.w600,
                                              fontSize: 15)),
                                    ),
                                    StatusChip.fromStatus(s.status ?? 'N/A'),
                                  ],
                                ),
                                const SizedBox(height: 6),
                                Text(
                                    '${s.patientName ?? ''} (${s.patientCode ?? ''})  •  ${s.surgeryName ?? ''}',
                                    style: const TextStyle(
                                        fontSize: 13, color: Colors.grey)),
                                const SizedBox(height: 4),
                                Text(
                                    'Surgeon: ${s.surgeonName ?? '-'}  •  ${s.surgeryDate ?? ''}',
                                    style: const TextStyle(
                                        fontSize: 12, color: Colors.grey)),
                                const SizedBox(height: 4),
                                Text(
                                    'Payable: ${_money(s.finalPayable)}',
                                    style: const TextStyle(
                                        fontSize: 12, color: Color(0xFF2E9E5B))),
                              ],
                            ),
                            ),
                          );
                        },
                      ),
                          ),
                        ),
                      ],
                    ),
    );
  }
}
