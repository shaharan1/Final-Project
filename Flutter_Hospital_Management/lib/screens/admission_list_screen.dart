import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:flutter_hospital_management/providers/admission_provider.dart';
import 'package:flutter_hospital_management/screens/admission_form_screen.dart';

class AdmissionListScreen extends ConsumerStatefulWidget {
  const AdmissionListScreen({super.key});

  @override
  ConsumerState<AdmissionListScreen> createState() =>
      _AdmissionListScreenState();
}

class _AdmissionListScreenState extends ConsumerState<AdmissionListScreen> {
  @override
  void initState() {
    super.initState();
    Future.microtask(
        () => ref.read(admissionNotifierProvider.notifier).load());
  }

  Color _statusColor(String? s) {
    switch ((s ?? '').toUpperCase()) {
      case 'ACTIVE':
      case 'ADMITTED':
        return Colors.blue;
      case 'DISCHARGED':
        return Colors.green;
      default:
        return Colors.grey;
    }
  }

  @override
  Widget build(BuildContext context) {
    final state = ref.watch(admissionNotifierProvider);

    return Scaffold(
      appBar: AppBar(
        title: const Text('Admissions'),
        backgroundColor: Colors.teal,
        foregroundColor: Colors.white,
      ),
      body: Column(
        children: [
          if (state.error != null)
            Padding(
              padding: const EdgeInsets.symmetric(horizontal: 12),
              child:
                  Text(state.error!, style: const TextStyle(color: Colors.red)),
            ),
          if (state.isLoading)
            const Expanded(child: Center(child: CircularProgressIndicator()))
          else if (state.admissions.isEmpty)
            const Expanded(child: Center(child: Text('No admissions found.')))
          else
            Expanded(
              child: ListView.separated(
                itemCount: state.admissions.length,
                separatorBuilder: (_, _) => const Divider(height: 1),
                itemBuilder: (context, i) {
                  final a = state.admissions[i];
                  final isActive =
                      (a.status ?? '').toUpperCase() == 'ACTIVE' ||
                          (a.status ?? '').toUpperCase() == 'ADMITTED';
                  return ListTile(
                    title: Text(a.patientName ?? 'Unknown'),
                    subtitle: Text(
                        'Bed ${a.assignedBedNumber ?? ''}  •  ${a.wardName ?? ''}  •  Dr. ${a.doctorName ?? ''}\nDiagnosis: ${a.initialDiagnosis ?? ''}'),
                    isThreeLine: true,
                    trailing: Row(
                      mainAxisSize: MainAxisSize.min,
                      children: [
                        Chip(
                          label: Text(a.status ?? ''),
                          backgroundColor: _statusColor(a.status).withValues(alpha: 0.15),
                        ),
                        if (isActive)
                          IconButton(
                            icon: const Icon(Icons.logout, color: Colors.green),
                            tooltip: 'Discharge',
                            onPressed: () async {
                              await ref
                                  .read(admissionNotifierProvider.notifier)
                                  .discharge(a.admissionId!);
                            },
                          ),
                      ],
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
          MaterialPageRoute(builder: (_) => const AdmissionFormScreen()),
        ),
      ),
    );
  }
}
