import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:flutter_hospital_management/providers/doctor_provider.dart';
import 'package:flutter_hospital_management/widgets/common.dart';
import 'package:flutter_hospital_management/widgets/app_drawer.dart';

class DoctorListScreen extends ConsumerStatefulWidget {
  const DoctorListScreen({super.key});

  @override
  ConsumerState<DoctorListScreen> createState() => _DoctorListScreenState();
}

class _DoctorListScreenState extends ConsumerState<DoctorListScreen> {
  @override
  void initState() {
    super.initState();
    Future.microtask(() => ref.read(doctorNotifierProvider.notifier).load());
  }

  @override
  Widget build(BuildContext context) {
    final state = ref.watch(doctorNotifierProvider);
    return Scaffold(
      appBar: AppBar(title: const Text('Doctors')),
      drawer: const AppDrawer(),
      body: state.isLoading
          ? const Center(child: CircularProgressIndicator())
          : state.error != null
              ? Center(child: Text(state.error!, style: const TextStyle(color: Colors.red)))
              : state.doctors.isEmpty
                  ? const EmptyState('No doctors found', icon: Icons.medical_services)
                  : RefreshIndicator(
                      onRefresh: () =>
                          ref.read(doctorNotifierProvider.notifier).load(),
                      child: ListView.separated(
                        padding: const EdgeInsets.all(16),
                        itemCount: state.doctors.length,
                        separatorBuilder: (_, _) => const SizedBox(height: 10),
                        itemBuilder: (_, i) {
                          final d = state.doctors[i];
                          return AppCard(
                            child: Row(
                              children: [
                                CircleAvatar(
                                  backgroundColor: const Color(0xFF0E7C86)
                                      .withValues(alpha: 0.12),
                                  child: const Icon(Icons.medical_services,
                                      color: Color(0xFF0E7C86)),
                                ),
                                const SizedBox(width: 14),
                                Expanded(
                                  child: Column(
                                    crossAxisAlignment:
                                        CrossAxisAlignment.start,
                                    children: [
                                      Text(d.name ?? 'Unknown',
                                          style: const TextStyle(
                                              fontWeight: FontWeight.w600,
                                              fontSize: 15)),
                                      const SizedBox(height: 4),
                                      Text(
                                        [
                                          if (d.specialization != null)
                                            d.specialization!,
                                          if (d.departmentName != null)
                                            d.departmentName!,
                                        ].join('  •  '),
                                        style: const TextStyle(
                                            fontSize: 13, color: Colors.grey),
                                      ),
                                      const SizedBox(height: 6),
                                      StatusChip.fromStatus(d.status ?? 'N/A'),
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
