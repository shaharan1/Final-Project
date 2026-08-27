import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:flutter_hospital_management/providers/ambulance_provider.dart';
import 'package:flutter_hospital_management/screens/ambulance_trip_detail_screen.dart';
import 'package:flutter_hospital_management/widgets/common.dart';
import 'package:flutter_hospital_management/widgets/app_drawer.dart';

class AmbulanceTripListScreen extends ConsumerStatefulWidget {
  const AmbulanceTripListScreen({super.key});

  @override
  ConsumerState<AmbulanceTripListScreen> createState() =>
      _AmbulanceTripListScreenState();
}

class _AmbulanceTripListScreenState
    extends ConsumerState<AmbulanceTripListScreen> {
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
    final trips = state.trips;
    final values = {
      for (final x in trips)
        if (x.status != null && x.status!.isNotEmpty) x.status!
    }.toList()
      ..sort();
    final chips = ['All', ...values];
    final filtered = _filter == 'All'
        ? trips
        : trips.where((x) => (x.status ?? '') == _filter).toList();
    final q = _search.toLowerCase();
    final results = q.isEmpty
        ? filtered
        : filtered
            .where((t) =>
                (t.ambulanceNumber ?? '').toLowerCase().contains(q) ||
                (t.pickupLocation ?? '').toLowerCase().contains(q) ||
                (t.dropoffLocation ?? '').toLowerCase().contains(q) ||
                (t.tripType ?? '').toLowerCase().contains(q))
            .toList();
    return Scaffold(
      appBar: AppBar(title: const Text('Ambulance Trips')),
      drawer: const AppDrawer(),
      body: state.isLoading
          ? const Center(child: CircularProgressIndicator())
          : state.error != null
              ? Center(
                  child: Text(state.error!,
                      style: const TextStyle(color: Colors.red)))
              : trips.isEmpty
                  ? const EmptyState('No trips found', icon: Icons.route)
                  : Column(
                      children: [
                        Padding(
                          padding: const EdgeInsets.fromLTRB(16, 12, 16, 4),
                          child: TextField(
                            decoration: const InputDecoration(
                              hintText: 'Search trips',
                              prefixIcon: Icon(Icons.search),
                              border: OutlineInputBorder(),
                            ),
                            onChanged: (v) => setState(() => _search = v),
                          ),
                        ),
                        _buildChips(chips),
                        Expanded(
                          child: results.isEmpty
                              ? const EmptyState('No matching trips',
                                  icon: Icons.route)
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
                                      final t = results[i];
                                      return GestureDetector(
                                        onTap: () => Navigator.push(
                                            context,
                                            MaterialPageRoute(
                                                builder: (_) =>
                                                    AmbulanceTripDetailScreen(
                                                        item: t))),
                                        child: AppCard(
                                          child: Column(
                                            crossAxisAlignment:
                                                CrossAxisAlignment.start,
                                            children: [
                                              Row(
                                                mainAxisAlignment:
                                                    MainAxisAlignment
                                                        .spaceBetween,
                                                children: [
                                                  Text(
                                                      t.ambulanceNumber ??
                                                          'Trip',
                                                      style: const TextStyle(
                                                          fontWeight:
                                                              FontWeight.w600,
                                                          fontSize: 15)),
                                                  StatusChip.fromStatus(
                                                      t.status ?? 'N/A'),
                                                ],
                                              ),
                                              const SizedBox(height: 6),
                                              Text(
                                                  '${t.pickupLocation ?? '-'}  →  ${t.dropoffLocation ?? '-'}',
                                                  style: const TextStyle(
                                                      fontSize: 13,
                                                      color: Colors.grey)),
                                              const SizedBox(height: 4),
                                              Text(
                                                  'Type: ${t.tripType ?? '-'}  •  Dist: ${t.distanceTravelled ?? 0} km',
                                                  style: const TextStyle(
                                                      fontSize: 12,
                                                      color: Colors.grey)),
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
