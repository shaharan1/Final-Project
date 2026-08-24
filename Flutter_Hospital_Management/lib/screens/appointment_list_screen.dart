import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:flutter_hospital_management/providers/appointment_provider.dart';
import 'package:flutter_hospital_management/screens/appointment_form_screen.dart';

class AppointmentListScreen extends ConsumerStatefulWidget {
  const AppointmentListScreen({super.key});

  @override
  ConsumerState<AppointmentListScreen> createState() =>
      _AppointmentListScreenState();
}

class _AppointmentListScreenState extends ConsumerState<AppointmentListScreen> {
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

  Color _statusColor(String? s) {
    switch (s) {
      case 'CANCELLED':
        return Colors.red;
      case 'COMPLETED':
        return Colors.green;
      case 'BOOKED':
        return Colors.blue;
      default:
        return Colors.grey;
    }
  }

  @override
  Widget build(BuildContext context) {
    final state = ref.watch(appointmentNotifierProvider);

    return Scaffold(
      appBar: AppBar(
        title: const Text('Appointments'),
        backgroundColor: Colors.teal,
        foregroundColor: Colors.white,
      ),
      body: Column(
        children: [
          Padding(
            padding: const EdgeInsets.all(12),
            child: TextField(
              controller: _search,
              decoration: InputDecoration(
                labelText: 'Search (number, name, phone)',
                prefixIcon: const Icon(Icons.search),
                border: const OutlineInputBorder(),
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
              padding: const EdgeInsets.symmetric(horizontal: 12),
              child: Text(state.error!,
                  style: const TextStyle(color: Colors.red)),
            ),
          if (state.isLoading)
            const Expanded(child: Center(child: CircularProgressIndicator()))
          else if (state.appointments.isEmpty)
            const Expanded(child: Center(child: Text('No appointments found.')))
          else
            Expanded(
              child: ListView.separated(
                itemCount: state.appointments.length,
                separatorBuilder: (_, _) => const Divider(height: 1),
                itemBuilder: (context, i) {
                  final a = state.appointments[i];
                  final isCancelled = a.status == 'CANCELLED';
                  return ListTile(
                    leading: CircleAvatar(
                      backgroundColor: _statusColor(a.status),
                      child: Text('${a.tokenNumber ?? a.serialNo ?? '?'}',
                          style: const TextStyle(color: Colors.white)),
                    ),
                    title: Text(a.patientName ?? 'Unknown'),
                    subtitle: Text(
                        '${a.doctorName ?? ''}  •  ${a.appointmentDate ?? ''} ${a.appointmentTime ?? ''}\nStatus: ${a.status ?? ''}  •  Fee: ${a.feeCharged ?? 0}'),
                    isThreeLine: true,
                    trailing: isCancelled
                        ? null
                        : IconButton(
                            icon: const Icon(Icons.cancel, color: Colors.red),
                            onPressed: () async {
                              await ref
                                  .read(appointmentNotifierProvider.notifier)
                                  .cancel(a.id!);
                            },
                          ),
                  );
                },
              ),
            ),
        ],
      ),
      floatingActionButton: FloatingActionButton(
        backgroundColor: Colors.teal,
        foregroundColor: Colors.white,
        child: const Icon(Icons.add),
        onPressed: () => Navigator.push(
          context,
          MaterialPageRoute(builder: (_) => const AppointmentFormScreen()),
        ),
      ),
    );
  }
}
