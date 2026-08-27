import 'package:flutter/material.dart';
import 'package:flutter_hospital_management/widgets/app_drawer.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:flutter_hospital_management/providers/appointment_provider.dart';
import 'package:flutter_hospital_management/screens/appointment_form_screen.dart';
import 'package:flutter_hospital_management/widgets/common.dart';

class AppointmentListScreen extends ConsumerStatefulWidget {
  const AppointmentListScreen({super.key});

  @override
  ConsumerState<AppointmentListScreen> createState() =>
      _AppointmentListScreenState();
}

class _AppointmentListScreenState
    extends ConsumerState<AppointmentListScreen> {
  final _search = TextEditingController();

  @override
  void initState() {
    super.initState();
    Future.microtask(
        () => ref.read(appointmentNotifierProvider.notifier).load());
  }

  @override
  void dispose() {
    _search.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    final state = ref.watch(appointmentNotifierProvider);

    return Scaffold(
      appBar: AppBar(title: const Text('Appointments'),
      drawer: const AppDrawer(),
      floatingActionButton: FloatingActionButton(
        child: const Icon(Icons.add),
        onPressed: () => Navigator.push(
          context,
          MaterialPageRoute(builder: (_) => const AppointmentFormScreen()),
        ).then((_) => ref.read(appointmentNotifierProvider.notifier).load()),
      ),
      body: Column(
        children: [
          Padding(
            padding: const EdgeInsets.all(16),
            child: TextField(
              controller: _search,
              decoration: InputDecoration(
                labelText: 'Search (number, name, phone)',
                prefixIcon: const Icon(Icons.search),
                suffixIcon: IconButton(
                  icon: const Icon(Icons.clear),
                  onPressed: () {
                    _search.clear();
                    ref.read(appointmentNotifierProvider.notifier).load();
                  },
                ),
              ),
              onChanged: (v) => ref
                  .read(appointmentNotifierProvider.notifier)
                  .search(v.trim()),
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
          else if (state.appointments.isEmpty)
            const Expanded(
                child:
                    EmptyState('No appointments found', icon: Icons.event_busy))
          else
            Expanded(
              child: RefreshIndicator(
                onRefresh: () =>
                    ref.read(appointmentNotifierProvider.notifier).load(),
                child: ListView.separated(
                padding: const EdgeInsets.symmetric(horizontal: 16),
                itemCount: state.appointments.length,
                separatorBuilder: (_, _) => const SizedBox(height: 10),
                itemBuilder: (context, i) {
                  final a = state.appointments[i];
                  final isCancelled = a.status == 'CANCELLED';
                  return AppCard(
                    child: Row(
                      children: [
                        CircleAvatar(
                          backgroundColor:
                              const Color(0xFF0E7C86).withValues(alpha: 0.12),
                          child: Text('${a.tokenNumber ?? a.serialNo ?? '?'}',
                              style: const TextStyle(
                                  color: Color(0xFF0E7C86),
                                  fontWeight: FontWeight.bold)),
                        ),
                        const SizedBox(width: 14),
                        Expanded(
                          child: Column(
                            crossAxisAlignment: CrossAxisAlignment.start,
                            children: [
                              Text(a.patientName ?? 'Unknown',
                                  style: const TextStyle(
                                      fontWeight: FontWeight.w600, fontSize: 15)),
                              const SizedBox(height: 4),
                              Text(
                                '${a.doctorName ?? ''}  •  ${a.appointmentDate ?? ''} ${a.appointmentTime ?? ''}',
                                style: const TextStyle(
                                    fontSize: 13, color: Colors.grey),
                              ),
                              const SizedBox(height: 6),
                              Row(
                                children: [
                                  StatusChip.fromStatus(a.status ?? 'N/A'),
                                  const SizedBox(width: 10),
                                  Text('Fee: ৳ ${(a.feeCharged ?? 0).toStringAsFixed(2)}',
                                      style: const TextStyle(
                                          fontSize: 12, color: Colors.grey)),
                                ],
                              ),
                            ],
                          ),
                        ),
                        if (!isCancelled)
                          IconButton(
                            icon: const Icon(Icons.cancel, color: Colors.red),
                            onPressed: () async {
                              await ref
                                  .read(appointmentNotifierProvider.notifier)
                                  .cancel(a.id!);
                            },
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
