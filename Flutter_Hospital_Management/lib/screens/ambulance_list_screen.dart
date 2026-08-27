import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:flutter_hospital_management/providers/ambulance_provider.dart';
import 'package:flutter_hospital_management/screens/ambulance_detail_screen.dart';
import 'package:flutter_hospital_management/widgets/common.dart';
import 'package:flutter_hospital_management/widgets/app_drawer.dart';

class AmbulanceListScreen extends ConsumerStatefulWidget {
  const AmbulanceListScreen({super.key});

  @override
  ConsumerState<AmbulanceListScreen> createState() =>
      _AmbulanceListScreenState();
}

class _AmbulanceListScreenState extends ConsumerState<AmbulanceListScreen> {
  String _filter = 'All';
  String _search = '';

  @override
  void initState() {
    super.initState();
    Future.microtask(
        () => ref.read(ambulanceNotifierProvider.notifier).load());
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
    final state = ref.watch(ambulanceNotifierProvider);
    final values = {
      for (final x in state.ambulances)
        if (x.status != null && x.status!.isNotEmpty) x.status!
    }.toList()
      ..sort();
    final chips = ['All', ...values];
    final filtered = _filter == 'All'
        ? state.ambulances
        : state.ambulances
            .where((x) => (x.status ?? '') == _filter)
            .toList();
    final q = _search.toLowerCase();
    final results = q.isEmpty
        ? filtered
        : filtered
            .where((a) =>
                (a.ambulanceNumber ?? '').toLowerCase().contains(q) ||
                (a.vehicleType ?? '').toLowerCase().contains(q) ||
                (a.driverName ?? '').toLowerCase().contains(q))
            .toList();
    return Scaffold(
      appBar: AppBar(title: const Text('Ambulances')),
      drawer: const AppDrawer(),
      body: state.isLoading
          ? const Center(child: CircularProgressIndicator())
          : state.error != null
              ? Center(
                  child: Text(state.error!,
                      style: const TextStyle(color: Colors.red)))
              : state.ambulances.isEmpty
                  ? const EmptyState('No ambulances found',
                      icon: Icons.emergency)
                  : Column(
                      children: [
                        Padding(
                          padding: const EdgeInsets.fromLTRB(16, 12, 16, 4),
                          child: TextField(
                            decoration: const InputDecoration(
                              hintText: 'Search ambulances',
                              prefixIcon: Icon(Icons.search),
                              border: OutlineInputBorder(),
                            ),
                            onChanged: (v) => setState(() => _search = v),
                          ),
                        ),
                        _buildChips(chips),
                        Expanded(
                          child: results.isEmpty
                              ? const EmptyState('No matching ambulances',
                                  icon: Icons.emergency)
                              : RefreshIndicator(
                                  onRefresh: () => ref
                                      .read(
                                          ambulanceNotifierProvider.notifier)
                                      .load(),
                                  child: ListView.separated(
                                    padding: const EdgeInsets.all(16),
                                    itemCount: results.length,
                                    separatorBuilder: (_, _) =>
                                        const SizedBox(height: 10),
                                    itemBuilder: (_, i) {
                                      final a = results[i];
                                      return AppCard(
                                        child: Row(
                                          children: [
                                            GestureDetector(
                                              onTap: () => Navigator.push(
                                                  context,
                                                  MaterialPageRoute(
                                                      builder: (_) =>
                                                          AmbulanceDetailScreen(
                                                              item: a))),
                                              child: CircleAvatar(
                                                backgroundColor: const Color(
                                                        0xFF0E7C86)
                                                    .withValues(alpha: 0.12),
                                                child: const Icon(
                                                    Icons.emergency,
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
                                                            AmbulanceDetailScreen(
                                                                item: a))),
                                                child: Column(
                                                  crossAxisAlignment:
                                                      CrossAxisAlignment.start,
                                                  children: [
                                                    Text(
                                                        a.ambulanceNumber ??
                                                            '#$i',
                                                        style: const TextStyle(
                                                            fontWeight:
                                                                FontWeight.w600,
                                                            fontSize: 15)),
                                                    const SizedBox(height: 4),
                                                    Text(
                                                      '${a.vehicleType ?? ''}  •  ${a.vehiclePlate ?? ''}',
                                                      style: const TextStyle(
                                                          fontSize: 13,
                                                          color: Colors.grey),
                                                    ),
                                                    const SizedBox(height: 4),
                                                    Text(
                                                        'Driver: ${a.driverName ?? '-'}  •  ${a.driverPhone ?? ''}',
                                                        style: const TextStyle(
                                                            fontSize: 12,
                                                            color:
                                                                Colors.grey)),
                                                    const SizedBox(height: 6),
                                                    StatusChip.fromStatus(
                                                        a.status ?? 'N/A'),
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
                      ],
                    ),
    );
  }
}
