import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:flutter_hospital_management/providers/insurance_provider.dart';
import 'package:flutter_hospital_management/widgets/common.dart';
import 'package:flutter_hospital_management/widgets/app_drawer.dart';
import 'package:flutter_hospital_management/screens/insurance_claim_detail_screen.dart';

class InsuranceClaimListScreen extends ConsumerStatefulWidget {
  const InsuranceClaimListScreen({super.key});

  @override
  ConsumerState<InsuranceClaimListScreen> createState() =>
      _InsuranceClaimListScreenState();
}

class _InsuranceClaimListScreenState
    extends ConsumerState<InsuranceClaimListScreen> {
  String _filter = 'All';

  @override
  void initState() {
    super.initState();
    Future.microtask(
        () => ref.read(insuranceNotifierProvider.notifier).load());
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

  String _money(dynamic v) =>
      '৳ ${(v is num ? v.toDouble() : 0).toStringAsFixed(2)}';

  @override
  Widget build(BuildContext context) {
    final state = ref.watch(insuranceNotifierProvider);
    final claims = state.claims;
    final values = {
      for (final x in claims)
        if (x.claimStatus != null && x.claimStatus!.isNotEmpty) x.claimStatus!
    }.toList()
      ..sort();
    final chips = ['All', ...values];
    final filtered = _filter == 'All'
        ? claims
        : claims.where((x) => (x.claimStatus ?? '') == _filter).toList();
    return Scaffold(
      appBar: AppBar(title: const Text('Insurance Claims')),
      drawer: const AppDrawer(),
      body: state.isLoading
          ? const Center(child: CircularProgressIndicator())
          : state.error != null
              ? Center(child: Text(state.error!, style: const TextStyle(color: Colors.red)))
              : claims.isEmpty
                  ? const EmptyState('No claims found', icon: Icons.receipt_long)
                  : Column(
                      children: [
                        _buildChips(chips),
                        Expanded(
                          child: RefreshIndicator(
                            onRefresh: () => ref
                                .read(insuranceNotifierProvider.notifier)
                                .load(),
                            child: ListView.separated(
                              padding: const EdgeInsets.all(16),
                              itemCount: filtered.length,
                              separatorBuilder: (_, _) =>
                                  const SizedBox(height: 10),
                              itemBuilder: (_, i) {
                                final c = filtered[i];
                                return AppCard(
                                  child: GestureDetector(
                                    onTap: () => Navigator.push(
                                        context,
                                        MaterialPageRoute(
                                            builder: (_) =>
                                                InsuranceClaimDetailScreen(
                                                    item: c))),
                                    child: Column(
                                      crossAxisAlignment:
                                          CrossAxisAlignment.start,
                                      children: [
                                        Row(
                                          mainAxisAlignment:
                                              MainAxisAlignment.spaceBetween,
                                          children: [
                                            Text(
                                                c.claimNumber ??
                                                    'Claim #${c.id}',
                                                style: const TextStyle(
                                                    fontWeight: FontWeight.w600,
                                                    fontSize: 15)),
                                            StatusChip.fromStatus(
                                                c.claimStatus ?? 'N/A'),
                                          ],
                                        ),
                                        const SizedBox(height: 6),
                                        Text(
                                            '${c.patientName ?? ''}  •  ${c.insuranceCompanyName ?? ''}',
                                            style: const TextStyle(
                                                fontSize: 13,
                                                color: Colors.grey)),
                                        const SizedBox(height: 4),
                                        Text(
                                            'Claim: ${_money(c.claimAmount)}  •  Approved: ${_money(c.approvedAmount)}',
                                            style: const TextStyle(
                                                fontSize: 12,
                                                color: Colors.grey)),
                                      ],
                                    ),
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
