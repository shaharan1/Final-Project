import 'package:flutter/material.dart';
import 'package:flutter_hospital_management/widgets/bottom_nav.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:flutter_hospital_management/models/test_order.dart';
import 'package:flutter_hospital_management/providers/lab_provider.dart';
import 'package:flutter_hospital_management/screens/test_order_detail_screen.dart';
import 'package:flutter_hospital_management/widgets/app_drawer.dart';
import 'package:flutter_hospital_management/widgets/common.dart';
import 'package:flutter_hospital_management/theme.dart';

class TestOrderListScreen extends ConsumerStatefulWidget {
  const TestOrderListScreen({super.key});

  @override
  ConsumerState<TestOrderListScreen> createState() =>
      _TestOrderListScreenState();
}

class _TestOrderListScreenState extends ConsumerState<TestOrderListScreen> {
  String _filter = 'All';

  @override
  void initState() {
    super.initState();
    Future.microtask(
        () => ref.read(testOrderNotifierProvider.notifier).load());
  }

  Future<void> _advance(TestOrder o) async {
    final action = TestOrderStatusHelper.nextAction(o.orderStatus);
    if (action == null) return;
    final body = <String, String>{};

    if (action['action'] == 'collect-sample') {
      final name = await _input('Collect Sample', 'Collector Name');
      if (name == null) return;
      final type = await _input('Collect Sample', 'Sample Type');
      if (type == null) return;
      body['collectorName'] = name;
      body['sampleType'] = type;
    } else if (action['action'] == 'receive-sample') {
      final by = await _input('Receive Sample', 'Received By');
      if (by == null) return;
      body['receivedBy'] = by;
    } else if (action['action'] == 'enter-result') {
      final val = await _input('Enter Result', 'Result Value');
      if (val == null) return;
      final notes = await _input('Enter Result', 'Result Notes');
      if (notes == null) return;
      final by = await _input('Enter Result', 'Entered By');
      if (by == null) return;
      body['resultValue'] = val;
      body['resultNotes'] = notes;
      body['enteredBy'] = by;
    } else if (action['action'] == 'verify') {
      final by = await _input('Verify Report', 'Verified By');
      if (by == null) return;
      final notes = await _input('Verify Report', 'Verification Notes');
      if (notes == null) return;
      body['verifiedBy'] = by;
      body['verificationNotes'] = notes;
    }

    await ref
        .read(testOrderNotifierProvider.notifier)
        .advance(o.id, action['action']!, body);
    if (mounted && ref.read(testOrderNotifierProvider).error != null) {
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(
            content: Text(ref.read(testOrderNotifierProvider).error!),
            backgroundColor: Colors.red),
      );
    }
  }

  Future<String?> _input(String title, String label) {
    final c = TextEditingController();
    return showDialog<String>(
      context: context,
      builder: (_) => AlertDialog(
        title: Text(title),
        content: TextField(
          controller: c,
          decoration: InputDecoration(labelText: label, border: const OutlineInputBorder()),
        ),
        actions: [
          TextButton(
              onPressed: () => Navigator.pop(context), child: const Text('Cancel')),
          ElevatedButton(
              onPressed: () => Navigator.pop(context, c.text.trim()),
              child: const Text('OK')),
        ],
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    final state = ref.watch(testOrderNotifierProvider);
    final values = {
      for (final x in state.orders)
        if (x.orderStatus != null && x.orderStatus!.isNotEmpty) x.orderStatus!
    }.toList()
      ..sort();
    final chips = ['All', ...values];
    final filtered = _filter == 'All'
        ? state.orders
        : state.orders
            .where((x) => (x.orderStatus ?? '') == _filter)
            .toList();
    return Scaffold(
      bottomNavigationBar: const RoleBottomNav(currentKey: 'testOrders'),
      appBar: AppBar(title: const Text('Lab Test Orders')),
      drawer: const AppDrawer(),
      body: state.isLoading
          ? const Center(child: CircularProgressIndicator())
          : state.error != null
              ? Center(child: Text(state.error!, style: const TextStyle(color: Colors.red)))
              : state.orders.isEmpty
                  ? const EmptyState('No test orders')
                  : Column(
                      children: [
                        _buildChips(chips),
                        Expanded(
                          child: RefreshIndicator(
                            onRefresh: () => ref
                                .read(testOrderNotifierProvider.notifier)
                                .load(),
                            child: ListView.separated(
                              padding: const EdgeInsets.all(16),
                              itemCount: filtered.length,
                              separatorBuilder: (_, _) =>
                                  const SizedBox(height: 10),
                              itemBuilder: (_, i) {
                                final o = filtered[i];
                                final action = TestOrderStatusHelper.nextAction(
                                    o.orderStatus);
                                return AppCard(
                                  child: Row(
                                    crossAxisAlignment:
                                        CrossAxisAlignment.center,
                                    children: [
                                      GestureDetector(
                                        onTap: () => Navigator.push(
                                          context,
                                          MaterialPageRoute(
                                              builder: (_) =>
                                                  TestOrderDetailScreen(
                                                      item: o))),
                                        child: CircleAvatar(
                                          backgroundColor: AppTheme.primary
                                              .withValues(alpha: 0.12),
                                          child: const Icon(
                                              Icons.science_outlined,
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
                                                    TestOrderDetailScreen(
                                                        item: o))),
                                          child: Column(
                                            crossAxisAlignment:
                                                CrossAxisAlignment.start,
                                            children: [
                                              Row(
                                                mainAxisAlignment:
                                                    MainAxisAlignment
                                                        .spaceBetween,
                                                children: [
                                                  Expanded(
                                                    child: Text(
                                                        o.testName ??
                                                            o.testCode ??
                                                            'Test',
                                                        style: const TextStyle(
                                                            fontWeight:
                                                                FontWeight
                                                                    .w700,
                                                            fontSize: 15)),
                                                  ),
                                                  StatusChip.fromStatus(
                                                      o.orderStatus ?? 'N/A'),
                                                ],
                                              ),
                                              const SizedBox(height: 4),
                                              Text(
                                                  '${o.patientName ?? 'Unknown'}  •  ${o.patientCode ?? ''}'
                                                  '${o.doctorName != null ? '  •  Dr. ${o.doctorName}' : ''}',
                                                  style: const TextStyle(
                                                      fontSize: 13,
                                                      color: Colors.grey)),
                                              const SizedBox(height: 10),
                                              if (action != null)
                                                Align(
                                                  alignment:
                                                      Alignment.centerRight,
                                                  child: ElevatedButton.icon(
                                                    onPressed: () =>
                                                        _advance(o),
                                                    icon: const Icon(
                                                        Icons.arrow_forward,
                                                        size: 16),
                                                    label: Text(action[
                                                        'label']!),
                                                    style: ElevatedButton
                                                        .styleFrom(
                                                            padding:
                                                                const EdgeInsets
                                                                    .symmetric(
                                                                    horizontal:
                                                                        14,
                                                                    vertical:
                                                                        8)),
                                                  ),
                                                )
                                              else
                                                const Align(
                                                  alignment:
                                                      Alignment.centerRight,
                                                  child: StatusChip(
                                                      'Completed',
                                                      Color(0xFF2E9E5B)),
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
