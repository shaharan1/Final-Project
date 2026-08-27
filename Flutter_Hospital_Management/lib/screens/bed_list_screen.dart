import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:flutter_hospital_management/providers/infrastructure_provider.dart';

class BedListScreen extends ConsumerStatefulWidget {
  const BedListScreen({super.key});

  @override
  ConsumerState<BedListScreen> createState() => _BedListScreenState();
}

class _BedListScreenState extends ConsumerState<BedListScreen> {
  String? _filter;

  @override
  void initState() {
    super.initState();
    Future.microtask(() => ref.read(bedNotifierProvider.notifier).load());
  }

  Color _statusColor(String? s) {
    switch ((s ?? '').toUpperCase()) {
      case 'AVAILABLE':
        return Colors.green;
      case 'OCCUPIED':
        return Colors.red;
      case 'MAINTENANCE':
        return Colors.orange;
      default:
        return Colors.grey;
    }
  }

  @override
  Widget build(BuildContext context) {
    final state = ref.watch(bedNotifierProvider);
    final beds = _filter == null
        ? state.beds
        : state.beds.where((b) => (b.status ?? '').toUpperCase() == _filter).toList();

    return Scaffold(
      appBar: AppBar(
        title: const Text('Beds'),
        backgroundColor: Colors.teal,
        foregroundColor: Colors.white,
      ),
      body: Column(
        children: [
          Padding(
            padding: const EdgeInsets.all(12),
            child: SingleChildScrollView(
              scrollDirection: Axis.horizontal,
              child: Row(
                children: [
                  FilterChip(
                    label: const Text('All'),
                    selected: _filter == null,
                    onSelected: (_) => setState(() => _filter = null),
                  ),
                  const SizedBox(width: 8),
                  FilterChip(
                    label: const Text('Available'),
                    selected: _filter == 'AVAILABLE',
                    onSelected: (_) => setState(() => _filter = 'AVAILABLE'),
                  ),
                  const SizedBox(width: 8),
                  FilterChip(
                    label: const Text('Occupied'),
                    selected: _filter == 'OCCUPIED',
                    onSelected: (_) => setState(() => _filter = 'OCCUPIED'),
                  ),
                  const SizedBox(width: 8),
                  FilterChip(
                    label: const Text('Maintenance'),
                    selected: _filter == 'MAINTENANCE',
                    onSelected: (_) => setState(() => _filter = 'MAINTENANCE'),
                  ),
                ],
              ),
            ),
          ),
          if (state.error != null)
            Padding(
              padding: const EdgeInsets.symmetric(horizontal: 12),
              child: Text(state.error!, style: const TextStyle(color: Colors.red)),
            ),
          if (state.isLoading)
            const Expanded(child: Center(child: CircularProgressIndicator()))
          else if (beds.isEmpty)
            const Expanded(child: Center(child: Text('No beds found.')))
          else
            Expanded(
              child: ListView.separated(
                itemCount: beds.length,
                separatorBuilder: (_, _) => const Divider(height: 1),
                itemBuilder: (context, i) {
                  final b = beds[i];
                  return ListTile(
                    leading: CircleAvatar(
                      backgroundColor: _statusColor(b.status),
                      child: Text((b.bedNumber ?? '?').length > 2
                          ? (b.bedNumber ?? '?').substring(0, 2)
                          : (b.bedNumber ?? '?'),
                          style: const TextStyle(color: Colors.white, fontSize: 12)),
                    ),
                    title: Text('Bed ${b.bedNumber ?? ''}'),
                    subtitle: Text(
                        '${b.wardName ?? 'Ward'}  •  ${b.roomType ?? ''}\nDaily: ${b.totalDailyCost ?? 0}'),
                    isThreeLine: true,
                    trailing: Chip(
                      label: Text(b.status ?? ''),
                      backgroundColor: _statusColor(b.status).withValues(alpha: 0.15),
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
