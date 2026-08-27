import 'package:flutter/material.dart';
import 'package:flutter_hospital_management/widgets/app_drawer.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:flutter_hospital_management/providers/admission_provider.dart';
import 'package:flutter_hospital_management/screens/admission_form_screen.dart';
import 'package:flutter_hospital_management/screens/admission_detail_screen.dart';
import 'package:flutter_hospital_management/widgets/common.dart';
import 'package:flutter_hospital_management/theme.dart';

class AdmissionListScreen extends ConsumerStatefulWidget {
  const AdmissionListScreen({super.key});

  @override
  ConsumerState<AdmissionListScreen> createState() =>
      _AdmissionListScreenState();
}

class _AdmissionListScreenState extends ConsumerState<AdmissionListScreen> {
  String _filter = 'All';

  @override
  void initState() {
    super.initState();
    Future.microtask(
        () => ref.read(admissionNotifierProvider.notifier).load());
  }

  @override
  Widget build(BuildContext context) {
    final state = ref.watch(admissionNotifierProvider);
    final values = {
      for (final a in state.admissions)
        if (a.status != null && a.status!.isNotEmpty) a.status!
    }.toList()
      ..sort();
    final chips = ['All', ...values];
    final filtered = _filter == 'All'
        ? state.admissions
        : state.admissions
            .where((a) => (a.status ?? '') == _filter)
            .toList();

    return Scaffold(
      appBar: AppBar(title: const Text('Admissions')),
      drawer: const AppDrawer(),
      floatingActionButton: FloatingActionButton(
        child: const Icon(Icons.add),
        onPressed: () => Navigator.push(
          context,
          MaterialPageRoute(builder: (_) => const AdmissionFormScreen()),
        ).then((_) => ref.read(admissionNotifierProvider.notifier).load()),
      ),
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
            const Expanded(
                child: EmptyState('No admissions found',
                    icon: Icons.assignment_ind))
          else
            Expanded(
              child: RefreshIndicator(
                onRefresh: () =>
                    ref.read(admissionNotifierProvider.notifier).load(),
                child: ListView.separated(
                padding: const EdgeInsets.symmetric(horizontal: 16),
                itemCount: filtered.length,
                separatorBuilder: (_, _) => const SizedBox(height: 10),
                itemBuilder: (context, i) {
                  final a = filtered[i];
                  final isActive = (a.status ?? '').toUpperCase() == 'ACTIVE' ||
                      (a.status ?? '').toUpperCase() == 'ADMITTED';
                  return AppCard(
                    child: Row(
                      children: [
                        GestureDetector(
                          onTap: () => Navigator.push(
                              context,
                              MaterialPageRoute(
                                  builder: (_) =>
                                      AdmissionDetailScreen(item: a))),
                          child: CircleAvatar(
                            backgroundColor:
                                AppTheme.primary.withValues(alpha: 0.12),
                            child: const Icon(Icons.assignment_ind,
                                color: AppTheme.primary),
                          ),
                        ),
                        const SizedBox(width: 14),
                        Expanded(
                          child: GestureDetector(
                            onTap: () => Navigator.push(
                                context,
                                MaterialPageRoute(
                                    builder: (_) =>
                                        AdmissionDetailScreen(item: a))),
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
