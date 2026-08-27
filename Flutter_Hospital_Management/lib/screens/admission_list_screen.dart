import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:flutter_hospital_management/providers/admission_provider.dart';
import 'package:flutter_hospital_management/screens/admission_form_screen.dart';
import 'package:flutter_hospital_management/widgets/common.dart';

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

  @override
  Widget build(BuildContext context) {
    final state = ref.watch(admissionNotifierProvider);

    return Scaffold(
      appBar: AppBar(title: const Text('Admissions')),
      floatingActionButton: FloatingActionButton(
        child: const Icon(Icons.add),
        onPressed: () => Navigator.push(
          context,
          MaterialPageRoute(builder: (_) => const AdmissionFormScreen()),
        ).then((_) => ref.read(admissionNotifierProvider.notifier).load()),
      ),
      body: Column(
        children: [
          if (state.error != null)
            Padding(
              padding: const EdgeInsets.symmetric(horizontal: 16),
              child: Text(state.error!,
                  style: const TextStyle(color: Colors.red)),
            ),
          if (state.isLoading)
            const Expanded(child: Center(child: CircularProgressIndicator()))
          else if (state.admissions.isEmpty)
            const Expanded(
                child: EmptyState('No admissions found',
                    icon: Icons.assignment_ind))
          else
            Expanded(
              child: ListView.separated(
                padding: const EdgeInsets.symmetric(horizontal: 16),
                itemCount: state.admissions.length,
                separatorBuilder: (_, _) => const SizedBox(height: 10),
                itemBuilder: (context, i) {
                  final a = state.admissions[i];
                  final isActive = (a.status ?? '').toUpperCase() == 'ACTIVE' ||
                      (a.status ?? '').toUpperCase() == 'ADMITTED';
                  return AppCard(
                    child: Row(
                      children: [
                        CircleAvatar(
                          backgroundColor:
                              const Color(0xFF0E7C86).withValues(alpha: 0.12),
                          child: const Icon(Icons.assignment_ind,
                              color: Color(0xFF0E7C86)),
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
                                'Bed ${a.assignedBedNumber ?? ''}  •  ${a.wardName ?? ''}  •  Dr. ${a.doctorName ?? ''}',
                                style: const TextStyle(
                                    fontSize: 13, color: Colors.grey),
                              ),
                              const SizedBox(height: 4),
                              Text(
                                'Diagnosis: ${a.initialDiagnosis ?? ''}',
                                style: const TextStyle(
                                    fontSize: 12, color: Colors.grey),
                                maxLines: 1,
                                overflow: TextOverflow.ellipsis,
                              ),
                              const SizedBox(height: 6),
                              StatusChip.fromStatus(a.status ?? 'N/A'),
                            ],
                          ),
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
    );
  }
}
