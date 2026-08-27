import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:flutter_hospital_management/core/role_access.dart';
import 'package:flutter_hospital_management/models/prescription.dart';
import 'package:flutter_hospital_management/providers/auth_provider.dart';
import 'package:flutter_hospital_management/providers/doctor_provider.dart';
import 'package:flutter_hospital_management/providers/prescription_provider.dart';
import 'package:flutter_hospital_management/screens/prescription_detail_screen.dart';
import 'package:flutter_hospital_management/widgets/app_drawer.dart';
import 'package:flutter_hospital_management/widgets/common.dart';
import 'package:flutter_hospital_management/theme.dart';

class PrescriptionListScreen extends ConsumerStatefulWidget {
  const PrescriptionListScreen({super.key});

  @override
  ConsumerState<PrescriptionListScreen> createState() =>
      _PrescriptionListScreenState();
}

class _PrescriptionListScreenState
    extends ConsumerState<PrescriptionListScreen> {
  @override
  void initState() {
    super.initState();
    _load();
  }

  void _load() {
    final role = normalizeRole(ref.read(authNotifierProvider).user?.role);
    if (role == 'doctor') {
      final userId = ref.read(authNotifierProvider).user?.userId;
      if (userId != null) {
        ref
            .read(doctorNotifierProvider.notifier)
            .loadMyProfile(userId)
            .then((_) {
          final id = ref.read(doctorNotifierProvider).myDoctor?.id;
          if (id != null) {
            ref.read(prescriptionNotifierProvider.notifier).loadByDoctor(id);
          }
        });
        return;
      }
    }
    ref.read(prescriptionNotifierProvider.notifier).loadAll();
  }

  @override
  Widget build(BuildContext context) {
    final state = ref.watch(prescriptionNotifierProvider);
    return Scaffold(
      appBar: AppBar(
        title: const Text('Prescriptions'),
        actions: [
          IconButton(
              icon: const Icon(Icons.refresh),
              onPressed: _load),
        ],
      ),
      drawer: const AppDrawer(),
      body: state.isLoading
          ? const Center(child: CircularProgressIndicator())
          : state.error != null
              ? Padding(
                  padding: const EdgeInsets.all(24),
                  child: Text(state.error!,
                      style: const TextStyle(color: AppTheme.danger)),
                )
              : state.prescriptions.isEmpty
                  ? const Center(
                      child: Text('No prescriptions found.',
                          style: TextStyle(color: Colors.grey)))
                  : ListView.separated(
                      padding: const EdgeInsets.all(16),
                      itemCount: state.prescriptions.length,
                      separatorBuilder: (_, __) => const SizedBox(height: 10),
                      itemBuilder: (_, i) {
                        final p = state.prescriptions[i];
                        return AppCard(
                          onTap: () => Navigator.push(
                            context,
                            MaterialPageRoute(
                              builder: (_) =>
                                  PrescriptionDetailScreen(id: p.id!),
                            ),
                          ),
                          child: ListTile(
                            leading: const Icon(Icons.receipt_long,
                                color: AppTheme.primary),
                            title: Text(p.patientName ?? 'Patient'),
                            subtitle: Text(
                                '${p.prescriptionNumber ?? ''}  •  ${p.createdDate ?? ''}'),
                            trailing: const Icon(Icons.chevron_right),
                          ),
                        );
                      },
                    ),
    );
  }
}
