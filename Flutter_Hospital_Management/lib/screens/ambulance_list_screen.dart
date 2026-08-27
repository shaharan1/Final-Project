import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:flutter_hospital_management/providers/ambulance_provider.dart';
import 'package:flutter_hospital_management/widgets/common.dart';
import 'package:flutter_hospital_management/widgets/app_drawer.dart';

class AmbulanceListScreen extends ConsumerStatefulWidget {
  const AmbulanceListScreen({super.key});

  @override
  ConsumerState<AmbulanceListScreen> createState() =>
      _AmbulanceListScreenState();
}

class _AmbulanceListScreenState extends ConsumerState<AmbulanceListScreen> {
  @override
  void initState() {
    super.initState();
    Future.microtask(
        () => ref.read(ambulanceNotifierProvider.notifier).load());
  }

  @override
  Widget build(BuildContext context) {
    final state = ref.watch(ambulanceNotifierProvider);
    return Scaffold(
      appBar: AppBar(title: const Text('Ambulances')),
      drawer: const AppDrawer(),
      body: state.isLoading
          ? const Center(child: CircularProgressIndicator())
          : state.error != null
              ? Center(child: Text(state.error!, style: const TextStyle(color: Colors.red)))
              : state.ambulances.isEmpty
                  ? const EmptyState('No ambulances found', icon: Icons.emergency)
                  : RefreshIndicator(
                      onRefresh: () =>
                          ref.read(ambulanceNotifierProvider.notifier).load(),
                      child: ListView.separated(
                        padding: const EdgeInsets.all(16),
                        itemCount: state.ambulances.length,
                        separatorBuilder: (_, _) => const SizedBox(height: 10),
                        itemBuilder: (_, i) {
                          final a = state.ambulances[i];
                          return AppCard(
                            child: Row(
                              children: [
                                CircleAvatar(
                                  backgroundColor: const Color(0xFF0E7C86)
                                      .withValues(alpha: 0.12),
                                  child: const Icon(Icons.emergency,
                                      color: Color(0xFF0E7C86)),
                                ),
                                const SizedBox(width: 14),
                                Expanded(
                                  child: Column(
                                    crossAxisAlignment:
                                        CrossAxisAlignment.start,
                                    children: [
                                      Text(a.ambulanceNumber ?? '#$i',
                                          style: const TextStyle(
                                              fontWeight: FontWeight.w600,
                                              fontSize: 15)),
                                      const SizedBox(height: 4),
                                      Text(
                                        '${a.vehicleType ?? ''}  •  ${a.vehiclePlate ?? ''}',
                                        style: const TextStyle(
                                            fontSize: 13, color: Colors.grey),
                                      ),
                                      const SizedBox(height: 4),
                                      Text(
                                          'Driver: ${a.driverName ?? '-'}  •  ${a.driverPhone ?? ''}',
                                          style: const TextStyle(
                                              fontSize: 12, color: Colors.grey)),
                                      const SizedBox(height: 6),
                                      StatusChip.fromStatus(a.status ?? 'N/A'),
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
