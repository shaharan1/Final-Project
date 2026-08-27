import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:flutter_hospital_management/providers/doctor_provider.dart';
import 'package:flutter_hospital_management/screens/doctor_detail_screen.dart';
import 'package:flutter_hospital_management/widgets/common.dart';
import 'package:flutter_hospital_management/widgets/app_drawer.dart';

class DoctorListScreen extends ConsumerStatefulWidget {
  const DoctorListScreen({super.key});

  @override
  ConsumerState<DoctorListScreen> createState() => _DoctorListScreenState();
}

class _DoctorListScreenState extends ConsumerState<DoctorListScreen> {
  String _filter = 'All';
  String _search = '';

  @override
  void initState() {
    super.initState();
    Future.microtask(() => ref.read(doctorNotifierProvider.notifier).load());
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
    final state = ref.watch(doctorNotifierProvider);
    final values = {
      for (final x in state.doctors)
        if (x.specialization != null && x.specialization!.isNotEmpty)
          x.specialization!
    }.toList()
      ..sort();
    final chips = ['All', ...values];
    final filtered = _filter == 'All'
        ? state.doctors
        : state.doctors
            .where((x) => (x.specialization ?? '') == _filter)
            .toList();
    final q = _search.toLowerCase();
    final results = q.isEmpty
        ? filtered
        : filtered
            .where((d) =>
                (d.name ?? '').toLowerCase().contains(q) ||
                (d.specialization ?? '').toLowerCase().contains(q) ||
                (d.departmentName ?? '').toLowerCase().contains(q))
            .toList();
    return Scaffold(
      appBar: AppBar(title: const Text('Doctors')),
      drawer: const AppDrawer(),
      body: state.isLoading
          ? const Center(child: CircularProgressIndicator())
          : state.error != null
              ? Center(
                  child: Text(state.error!,
                      style: const TextStyle(color: Colors.red)))
              : state.doctors.isEmpty
                  ? const EmptyState('No doctors found',
                      icon: Icons.medical_services)
                  : Column(
                      children: [
                        Padding(
                          padding: const EdgeInsets.fromLTRB(16, 12, 16, 4),
                          child: TextField(
                            decoration: const InputDecoration(
                              hintText: 'Search doctors',
                              prefixIcon: Icon(Icons.search),
                              border: OutlineInputBorder(),
                            ),
                            onChanged: (v) => setState(() => _search = v),
                          ),
                        ),
                        _buildChips(chips),
                        Expanded(
                          child: results.isEmpty
                              ? const EmptyState('No matching doctors',
                                  icon: Icons.medical_services)
                              : RefreshIndicator(
                                  onRefresh: () => ref
                                      .read(doctorNotifierProvider.notifier)
                                      .load(),
                                  child: ListView.separated(
                                    padding: const EdgeInsets.all(16),
                                    itemCount: results.length,
                                    separatorBuilder: (_, _) =>
                                        const SizedBox(height: 10),
                                    itemBuilder: (_, i) {
                                      final d = results[i];
                                      return AppCard(
                                        child: Row(
                                          children: [
                                            GestureDetector(
                                              onTap: () => Navigator.push(
                                                  context,
                                                  MaterialPageRoute(
                                                      builder: (_) =>
                                                          DoctorDetailScreen(
                                                              item: d))),
                                              child: CircleAvatar(
                                                backgroundColor: const Color(
                                                        0xFF0E7C86)
                                                    .withValues(alpha: 0.12),
                                                child: const Icon(
                                                    Icons.medical_services,
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
                                                            DoctorDetailScreen(
                                                                item: d))),
                                                child: Column(
                                                  crossAxisAlignment:
                                                      CrossAxisAlignment.start,
                                                  children: [
                                                    Text(d.name ?? 'Unknown',
                                                        style: const TextStyle(
                                                            fontWeight:
                                                                FontWeight.w600,
                                                            fontSize: 15)),
                                                    const SizedBox(height: 4),
                                                    Text(
                                                      [
                                                        if (d.specialization !=
                                                            null)
                                                          d.specialization!,
                                                        if (d.departmentName !=
                                                            null)
                                                          d.departmentName!,
                                                      ].join('  •  '),
                                                      style: const TextStyle(
                                                          fontSize: 13,
                                                          color: Colors.grey),
                                                    ),
                                                    const SizedBox(height: 6),
                                                    StatusChip.fromStatus(
                                                        d.status ?? 'N/A'),
                                                  ],
                                                ),
                                              ),
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
