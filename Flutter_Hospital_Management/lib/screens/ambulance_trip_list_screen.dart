import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:flutter_hospital_management/providers/ambulance_provider.dart';
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
  @override
  void initState() {
    super.initState();
    Future.microtask(
        () => ref.read(ambulanceNotifierProvider.notifier).load());
  }

  @override
  Widget build(BuildContext context) {
    final state = ref.watch(ambulanceNotifierProvider);
    final trips = state.trips;
    return Scaffold(
      appBar: AppBar(title: const Text('Ambulance Trips')),
      drawer: const AppDrawer(),
      body: state.isLoading
          ? const Center(child: CircularProgressIndicator())
          : state.error != null
              ? Center(child: Text(state.error!, style: const TextStyle(color: Colors.red)))
              : trips.isEmpty
                  ? const EmptyState('No trips found', icon: Icons.route)
                  : RefreshIndicator(
                      onRefresh: () =>
                          ref.read(ambulanceNotifierProvider.notifier).load(),
                      child: ListView.separated(
                        padding: const EdgeInsets.all(16),
                        itemCount: trips.length,
                        separatorBuilder: (_, _) => const SizedBox(height: 10),
                        itemBuilder: (_, i) {
                          final t = trips[i];
                          return AppCard(
                            child: Column(
                              crossAxisAlignment: CrossAxisAlignment.start,
                              children: [
                                Row(
                                  mainAxisAlignment:
                                      MainAxisAlignment.spaceBetween,
                                  children: [
                                    Text(t.ambulanceNumber ?? 'Trip',
                                        style: const TextStyle(
                                            fontWeight: FontWeight.w600,
                                            fontSize: 15)),
                                    StatusChip.fromStatus(t.status ?? 'N/A'),
                                  ],
                                ),
                                const SizedBox(height: 6),
                                Text(
                                    '${t.pickupLocation ?? '-'}  →  ${t.dropoffLocation ?? '-'}',
                                    style: const TextStyle(
                                        fontSize: 13, color: Colors.grey)),
                                const SizedBox(height: 4),
                                Text(
                                    'Type: ${t.tripType ?? '-'}  •  Dist: ${t.distanceTravelled ?? 0} km',
                                    style: const TextStyle(
                                        fontSize: 12, color: Colors.grey)),
                              ],
                            ),
                          );
                        },
                      ),
                    ),
    );
  }
}
