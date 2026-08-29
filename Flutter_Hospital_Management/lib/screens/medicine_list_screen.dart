import 'package:flutter/material.dart';
import 'package:flutter_hospital_management/widgets/bottom_nav.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:flutter_hospital_management/providers/pharmacy_provider.dart';
import 'package:flutter_hospital_management/screens/medicine_form_screen.dart';
import 'package:flutter_hospital_management/screens/medicine_detail_screen.dart';
import 'package:flutter_hospital_management/widgets/app_drawer.dart';
import 'package:flutter_hospital_management/widgets/common.dart';
import 'package:flutter_hospital_management/theme.dart';

class MedicineListScreen extends ConsumerStatefulWidget {
  const MedicineListScreen({super.key});

  @override
  ConsumerState<MedicineListScreen> createState() => _MedicineListScreenState();
}

class _MedicineListScreenState extends ConsumerState<MedicineListScreen> {
  final _search = TextEditingController();
  String _filter = 'All';

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
  void initState() {
    super.initState();
    Future.microtask(
        () => ref.read(medicineNotifierProvider.notifier).load());
  }

  @override
  Widget build(BuildContext context) {
    final state = ref.watch(medicineNotifierProvider);
    final values = {
      for (final x in state.medicines)
        if (x.genericName != null && x.genericName!.isNotEmpty) x.genericName!
    }.toList()
      ..sort();
    final chips = ['All', ...values];
    final filtered = _filter == 'All'
        ? state.medicines
        : state.medicines
            .where((x) => (x.genericName ?? '') == _filter)
            .toList();
    return Scaffold(
      bottomNavigationBar: const RoleBottomNav(currentKey: 'pharmacy'),
      appBar: AppBar(title: const Text('Medicines')),
      drawer: const AppDrawer(),
      floatingActionButton: FloatingActionButton(
        child: const Icon(Icons.add),
        onPressed: () => Navigator.push(
          context,
          MaterialPageRoute(builder: (_) => const MedicineFormScreen()),
        ).then((_) => ref.read(medicineNotifierProvider.notifier).load()),
      ),
      body: Column(
        children: [
          Padding(
            padding: const EdgeInsets.all(16),
            child: TextField(
              controller: _search,
              decoration: InputDecoration(
                hintText: 'Search medicines...',
                prefixIcon: const Icon(Icons.search),
                suffixIcon: IconButton(
                  icon: const Icon(Icons.clear),
                  onPressed: () {
                    _search.clear();
                    ref.read(medicineNotifierProvider.notifier).search('');
                  },
                ),
              ),
              onChanged: (v) =>
                  ref.read(medicineNotifierProvider.notifier).search(v),
            ),
          ),
          _buildChips(chips),
          Expanded(
            child: state.isLoading
                ? const Center(child: CircularProgressIndicator())
                : state.error != null
                    ? Center(child: Text(state.error!, style: const TextStyle(color: Colors.red)))
                    : filtered.isEmpty
                        ? const EmptyState('No medicines found')
                        : ListView.separated(
                            padding: const EdgeInsets.symmetric(horizontal: 16),
                            itemCount: filtered.length,
                            separatorBuilder: (_, _) => const SizedBox(height: 10),
                            itemBuilder: (_, i) {
                              final m = filtered[i];
                              return AppCard(
                                child: Row(
                                  children: [
                                    GestureDetector(
                                      onTap: () => Navigator.push(
                                        context,
                                        MaterialPageRoute(
                                            builder: (_) =>
                                                MedicineDetailScreen(item: m)),
                                      ),
                                      child: CircleAvatar(
                                        backgroundColor: AppTheme
                                            .primary
                                            .withValues(alpha: 0.12),
                                        child: const Icon(Icons.medication,
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
                                                  MedicineDetailScreen(item: m)),
                                        ),
                                        child: Column(
                                          crossAxisAlignment:
                                              CrossAxisAlignment.start,
                                          children: [
                                            Text(m.medicineName,
                                                style: const TextStyle(
                                                    fontWeight: FontWeight.w600,
                                                    fontSize: 15)),
                                            const SizedBox(height: 4),
                                            Text(
                                              [
                                                if (m.genericName != null)
                                                  'Generic: ${m.genericName}',
                                                if (m.dosage != null)
                                                  'Dosage: ${m.dosage}',
                                              ].join('  •  '),
                                              style: const TextStyle(
                                                  fontSize: 13,
                                                  color: Colors.grey),
                                            ),
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
        ],
      ),
    );
  }
}
