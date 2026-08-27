import 'package:flutter/material.dart';
import 'package:flutter_hospital_management/widgets/app_drawer.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:flutter_hospital_management/providers/infrastructure_provider.dart';
import 'package:flutter_hospital_management/screens/bed_detail_screen.dart';
import 'package:flutter_hospital_management/widgets/common.dart';
import 'package:flutter_hospital_management/theme.dart';

class BedListScreen extends ConsumerStatefulWidget {
  const BedListScreen({super.key});

  @override
  ConsumerState<BedListScreen> createState() => _BedListScreenState();
}

class _BedListScreenState extends ConsumerState<BedListScreen> {
  String _filter = 'All';

  @override
  void initState() {
    super.initState();
    Future.microtask(() => ref.read(bedNotifierProvider.notifier).load());
  }

  @override
  Widget build(BuildContext context) {
    final state = ref.watch(bedNotifierProvider);
    final values = {
      for (final b in state.beds)
        if (b.status != null && b.status!.isNotEmpty) b.status!
    }.toList()
      ..sort();
    final chips = ['All', ...values];
    final filtered = _filter == 'All'
        ? state.beds
        : state.beds.where((b) => (b.status ?? '') == _filter).toList();

    return Scaffold(
      appBar: AppBar(title: const Text('Beds')),
      drawer: const AppDrawer(),
      body: Column(
        children: [
          _buildChips(chips),
          if (state.error != null)
            Padding(
              padding: const EdgeInsets.symmetric(horizontal: 16),
              child: Text(state.error!,
                  style: const TextStyle(color: Colors.red)),
            ),
          if (state.isLoading)
            const Expanded(child: Center(child: CircularProgressIndicator()))
          else if (filtered.isEmpty)
            const Expanded(child: EmptyState('No beds found', icon: Icons.bed))
          else
            Expanded(
              child: RefreshIndicator(
                onRefresh: () => ref.read(bedNotifierProvider.notifier).load(),
                child: ListView.separated(
                padding: const EdgeInsets.symmetric(horizontal: 16),
                itemCount: filtered.length,
                separatorBuilder: (_, _) => const SizedBox(height: 10),
                itemBuilder: (context, i) {
                  final b = filtered[i];
                  return AppCard(
                    child: Row(
                      children: [
                        GestureDetector(
                          onTap: () => Navigator.push(
                              context,
                              MaterialPageRoute(
                                  builder: (_) => BedDetailScreen(item: b))),
                          child: CircleAvatar(
                            backgroundColor:
                                AppTheme.primary.withValues(alpha: 0.12),
                            child: const Icon(Icons.bed, color: AppTheme.primary),
                          ),
                        ),
                        const SizedBox(width: 14),
                        Expanded(
                          child: GestureDetector(
                            onTap: () => Navigator.push(
                                context,
                                MaterialPageRoute(
                                    builder: (_) => BedDetailScreen(item: b))),
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
}
