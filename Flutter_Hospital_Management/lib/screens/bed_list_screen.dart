import 'package:flutter/material.dart';
import 'package:flutter_hospital_management/widgets/app_drawer.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:flutter_hospital_management/providers/infrastructure_provider.dart';
import 'package:flutter_hospital_management/widgets/common.dart';

class BedListScreen extends ConsumerStatefulWidget {
  const BedListScreen({super.key});

  @override
  ConsumerState<BedListScreen> createState() => _BedListScreenState();
}

class _BedListScreenState extends ConsumerState<BedListScreen> {
  String? _filter;

  @override
  void initState() {
    super.initState();
    Future.microtask(() => ref.read(bedNotifierProvider.notifier).load());
  }

  @override
  Widget build(BuildContext context) {
    final state = ref.watch(bedNotifierProvider);
    final beds = _filter == null
        ? state.beds
        : state.beds
            .where((b) =>
                (b.status ?? '').toUpperCase() == _filter)
            .toList();

    return Scaffold(
      appBar: AppBar(title: const Text('Beds')),
      drawer: const AppDrawer(),
      body: Column(
        children: [
          Padding(
            padding: const EdgeInsets.all(16),
            child: SingleChildScrollView(
              scrollDirection: Axis.horizontal,
              child: Row(
                children: [
                  _filterChip('All', _filter == null, () => setState(() => _filter = null)),
                  const SizedBox(width: 8),
                  _filterChip('Available', _filter == 'AVAILABLE',
                      () => setState(() => _filter = 'AVAILABLE')),
                  const SizedBox(width: 8),
                  _filterChip('Occupied', _filter == 'OCCUPIED',
                      () => setState(() => _filter = 'OCCUPIED')),
                  const SizedBox(width: 8),
                  _filterChip('Maintenance', _filter == 'MAINTENANCE',
                      () => setState(() => _filter = 'MAINTENANCE')),
                ],
              ),
            ),
          ),
          if (state.error != null)
            Padding(
              padding: const EdgeInsets.symmetric(horizontal: 16),
              child: Text(state.error!,
                  style: const TextStyle(color: Colors.red)),
            ),
          if (state.isLoading)
            const Expanded(child: Center(child: CircularProgressIndicator()))
          else if (beds.isEmpty)
            const Expanded(child: EmptyState('No beds found', icon: Icons.bed))
          else
            Expanded(
              child: RefreshIndicator(
                onRefresh: () => ref.read(bedNotifierProvider.notifier).load(),
                child: ListView.separated(
                padding: const EdgeInsets.symmetric(horizontal: 16),
                itemCount: beds.length,
                separatorBuilder: (_, _) => const SizedBox(height: 10),
                itemBuilder: (context, i) {
                  final b = beds[i];
                  return AppCard(
                    child: Row(
                      children: [
                        CircleAvatar(
                          backgroundColor:
                              const Color(0xFF0E7C86).withValues(alpha: 0.12),
                          child: const Icon(Icons.bed, color: Color(0xFF0E7C86)),
                        ),
                        const SizedBox(width: 14),
                        Expanded(
                          child: Column(
                            crossAxisAlignment: CrossAxisAlignment.start,
                            children: [
                              Text('Bed ${b.bedNumber ?? ''}',
                                  style: const TextStyle(
                                      fontWeight: FontWeight.w600, fontSize: 15)),
                              const SizedBox(height: 4),
                              Text(
                                '${b.wardName ?? 'Ward'}  •  ${b.roomType ?? ''}',
                                style: const TextStyle(
                                    fontSize: 13, color: Colors.grey),
                              ),
                              const SizedBox(height: 4),
                              Text(
                                  'Daily: ৳ ${(b.totalDailyCost ?? 0).toStringAsFixed(2)}',
                                  style: const TextStyle(
                                      fontSize: 12, color: Colors.grey)),
                            ],
                          ),
                        ),
                        StatusChip.fromStatus(b.status ?? 'N/A'),
                      ],
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

  Widget _filterChip(String label, bool selected, VoidCallback onTap) =>
      FilterChip(
        label: Text(label),
        selected: selected,
        onSelected: (_) => onTap(),
        selectedColor: const Color(0xFF0E7C86).withValues(alpha: 0.2),
        checkmarkColor: const Color(0xFF0E7C86),
        labelStyle: TextStyle(
          color: selected ? const Color(0xFF0E7C86) : Colors.grey.shade700,
          fontWeight: FontWeight.w600,
        ),
      );
}
