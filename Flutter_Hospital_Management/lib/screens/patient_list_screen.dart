import 'package:flutter/material.dart';
import 'package:flutter_hospital_management/widgets/bottom_nav.dart';
import 'package:flutter_hospital_management/widgets/app_drawer.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:flutter_hospital_management/models/patient.dart';
import 'package:flutter_hospital_management/providers/patient_provider.dart';
import 'package:flutter_hospital_management/screens/patient_form_screen.dart';
import 'package:flutter_hospital_management/screens/patient_detail_screen.dart';
import 'package:flutter_hospital_management/widgets/common.dart';

class PatientListScreen extends ConsumerStatefulWidget {
  const PatientListScreen({super.key});

  @override
  ConsumerState<PatientListScreen> createState() => _PatientListScreenState();
}

class _PatientListScreenState extends ConsumerState<PatientListScreen> {
  final _searchController = TextEditingController();
  String _filter = 'All';

  @override
  void initState() {
    super.initState();
    Future.microtask(
        () => ref.read(patientNotifierProvider.notifier).load());
  }

  @override
  void dispose() {
    _searchController.dispose();
    super.dispose();
  }

  void _confirmDelete(Patient p) {
    showDialog(
      context: context,
      builder: (ctx) => AlertDialog(
        title: const Text('Delete patient?'),
        content: Text('Delete ${p.name} (${p.patientCode ?? p.id})?'),
        actions: [
          TextButton(
            onPressed: () => Navigator.pop(ctx),
            child: const Text('Cancel'),
          ),
          TextButton(
            onPressed: () async {
              Navigator.pop(ctx);
              await ref.read(patientNotifierProvider.notifier).delete(p.id!);
            },
            child: const Text('Delete', style: TextStyle(color: Colors.red)),
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

  @override
  Widget build(BuildContext context) {
    final state = ref.watch(patientNotifierProvider);
    final genders = {
      for (final p in state.patients)
        if (p.gender != null && p.gender!.isNotEmpty) p.gender!
    }.toList()
      ..sort();
    final chips = ['All', ...genders];
    final filtered = _filter == 'All'
        ? state.patients
        : state.patients
            .where((p) => (p.gender ?? '') == _filter)
            .toList();

    return Scaffold(
      bottomNavigationBar: const RoleBottomNav(currentKey: 'patients'),
      appBar: AppBar(title: const Text('Patients')),
      drawer: const AppDrawer(),
      floatingActionButton: FloatingActionButton(
        child: const Icon(Icons.add),
        onPressed: () => Navigator.push(
          context,
          MaterialPageRoute(builder: (_) => const PatientFormScreen()),
        ).then((_) => ref.read(patientNotifierProvider.notifier).load()),
      ),
      body: Column(
        children: [
          Padding(
            padding: const EdgeInsets.all(16),
            child: TextField(
              controller: _searchController,
              decoration: InputDecoration(
                labelText: 'Search (name, phone, code...)',
                prefixIcon: const Icon(Icons.search),
                suffixIcon: IconButton(
                  icon: const Icon(Icons.clear),
                  onPressed: () {
                    _searchController.clear();
                    ref.read(patientNotifierProvider.notifier).load();
                  },
                ),
              ),
                  onChanged: (v) =>
                      ref.read(patientNotifierProvider.notifier).search(v.trim()),
            ),
          ),
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
                child: EmptyState('No patients found', icon: Icons.people))
          else
            Expanded(
              child: RefreshIndicator(
                onRefresh: () =>
                    ref.read(patientNotifierProvider.notifier).load(),
                child: ListView.separated(
                padding: const EdgeInsets.symmetric(horizontal: 16),
                itemCount: filtered.length,
                separatorBuilder: (_, _) => const SizedBox(height: 10),
                itemBuilder: (context, i) {
                  final p = filtered[i];
                  return AppCard(
                    child: Row(
                      children: [
                        GestureDetector(
                          onTap: () => Navigator.push(
                            context,
                            MaterialPageRoute(
                              builder: (_) =>
                                  PatientDetailScreen(patientId: p.id!),
                            ),
                          ),
                          child: CircleAvatar(
                            backgroundColor: const Color(0xFF0E7C86)
                                .withValues(alpha: 0.12),
                            child: const Icon(Icons.person,
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
                                    PatientDetailScreen(patientId: p.id!),
                              ),
                            ),
                            child: Column(
                              crossAxisAlignment: CrossAxisAlignment.start,
                              children: [
                                Text(p.name,
                                    style: const TextStyle(
                                        fontWeight: FontWeight.w600,
                                        fontSize: 15)),
                                const SizedBox(height: 4),
                                Text(
                                  [
                                    if (p.patientCode != null) p.patientCode!,
                                    if (p.phone != null) p.phone!,
                                    if (p.gender != null) p.gender!,
                                  ].join('  •  '),
                                  style: const TextStyle(
                                      fontSize: 13, color: Colors.grey),
                                ),
                              ],
                            ),
                          ),
                        ),
                        IconButton(
                          icon: const Icon(Icons.edit, color: Colors.blue),
                          onPressed: () => Navigator.push(
                            context,
                            MaterialPageRoute(
                              builder: (_) => PatientFormScreen(patient: p),
                            ),
                          ).then((_) =>
                              ref.read(patientNotifierProvider.notifier).load()),
                        ),
                        IconButton(
                          icon: const Icon(Icons.delete, color: Colors.red),
                          onPressed: () => _confirmDelete(p),
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
