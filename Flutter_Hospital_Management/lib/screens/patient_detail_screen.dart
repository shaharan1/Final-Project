import 'package:flutter/material.dart';
import 'package:flutter_hospital_management/widgets/app_drawer.dart';
import 'package:flutter_hospital_management/providers/patient_provider.dart';
import 'package:flutter_hospital_management/models/patient.dart';
import 'package:flutter_hospital_management/screens/patient_form_screen.dart';
import 'package:flutter_hospital_management/widgets/common.dart';
import 'package:flutter_hospital_management/theme.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';

class PatientDetailScreen extends ConsumerStatefulWidget {
  final int patientId;
  const PatientDetailScreen({super.key, required this.patientId});

  @override
  ConsumerState<PatientDetailScreen> createState() =>
      _PatientDetailScreenState();
}

class _PatientDetailScreenState extends ConsumerState<PatientDetailScreen> {
  Patient? _patient;
  bool _loading = true;
  String? _error;

  @override
  void initState() {
    super.initState();
    _load();
  }

  Future<void> _load() async {
    setState(() {
      _loading = true;
      _error = null;
    });
    try {
      final p = await ref
          .read(patientServiceProvider)
          .getById(widget.patientId);
      setState(() {
        _patient = p;
        _loading = false;
      });
    } catch (e) {
      setState(() {
        _error = e.toString();
        _loading = false;
      });
    }
  }

  void _confirmDelete() {
    showDialog(
      context: context,
      builder: (ctx) => AlertDialog(
        title: const Text('Delete patient?'),
        content: Text('Delete ${_patient!.name}? This cannot be undone.'),
        actions: [
          TextButton(
              onPressed: () => Navigator.pop(ctx), child: const Text('Cancel')),
          TextButton(
            onPressed: () async {
              Navigator.pop(ctx);
              await ref
                  .read(patientNotifierProvider.notifier)
                  .delete(_patient!.id!);
              if (mounted) Navigator.pop(context);
            },
            child: const Text('Delete',
                style: TextStyle(color: Colors.red)),
          ),
        ],
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    final p = _patient;
    return Scaffold(
      appBar: AppBar(
        title: Text(p?.name ?? 'Patient'),
        actions: [
          if (p != null) ...[
            IconButton(
              icon: const Icon(Icons.edit),
              tooltip: 'Edit',
              onPressed: () => Navigator.push(
                context,
                MaterialPageRoute(
                  builder: (_) => PatientFormScreen(patient: p),
                ),
              ).then((_) => _load()),
            ),
            IconButton(
              icon: const Icon(Icons.delete),
              tooltip: 'Delete',
              onPressed: _confirmDelete,
            ),
          ],
          IconButton(
            icon: const Icon(Icons.refresh),
            onPressed: _load,
          ),
        ],
      ),
      drawer: const AppDrawer(),
      body: _loading
          ? const Center(child: CircularProgressIndicator())
          : _error != null
              ? Center(
                  child: Padding(
                  padding: const EdgeInsets.all(24),
                  child: Text(_error!,
                      style: const TextStyle(color: Colors.red)),
                ))
              : RefreshIndicator(
                  onRefresh: _load,
                  child: ListView(
                    padding: const EdgeInsets.all(16),
                    children: [
                      Center(
                        child: Column(
                          children: [
                            CircleAvatar(
                              radius: 38,
                              backgroundColor:
                                  AppTheme.primary.withValues(alpha: 0.12),
                              child: const Icon(Icons.person,
                                  size: 40, color: AppTheme.primary),
                            ),
                            const SizedBox(height: 10),
                            Text(p!.name,
                                style: const TextStyle(
                                    fontSize: 20,
                                    fontWeight: FontWeight.bold)),
                            const SizedBox(height: 4),
                            Text(
                              [
                                if (p.patientCode != null) p.patientCode!,
                                if (p.gender != null) p.gender!,
                                if (p.bloodGroup != null) p.bloodGroup!,
                              ].join('  •  '),
                              style: const TextStyle(color: Colors.grey),
                            ),
                          ],
                        ),
                      ),
                      const SizedBox(height: 20),
                      DetailSection('Personal Information', [
                        DetailRow('Patient Code', p.patientCode,
                            icon: Icons.badge),
                        DetailRow('Date of Birth', p.dateOfBirth,
                            icon: Icons.cake),
                        DetailRow('Gender', p.gender, icon: Icons.wc),
                        DetailRow('Blood Group', p.bloodGroup,
                            icon: Icons.bloodtype),
                        DetailRow('Marital Status', p.maritalStatus,
                            icon: Icons.favorite),
                        DetailRow('National ID', p.nationalId,
                            icon: Icons.credit_card),
                      ], icon: Icons.person),
                      DetailSection('Contact', [
                        DetailRow('Phone', p.phone,
                            icon: Icons.phone),
                        DetailRow('Alternate Phone', p.alternatePhone,
                            icon: Icons.phone_android),
                        DetailRow('Email', p.email, icon: Icons.email),
                        DetailRow('Address', p.address,
                            icon: Icons.home),
                        DetailRow('City', p.city, icon: Icons.location_city),
                        DetailRow('District', p.district,
                            icon: Icons.map),
                        DetailRow('Postal Code', p.postalCode,
                            icon: Icons.local_post_office),
                      ], icon: Icons.contact_phone),
                      DetailSection('Emergency Contact', [
                        DetailRow('Name', p.emergencyContactName,
                            icon: Icons.person_pin),
                        DetailRow('Number', p.emergencyContactNumber,
                            icon: Icons.phone),
                        DetailRow('Relationship', p.relationship,
                            icon: Icons.people),
                      ], icon: Icons.emergency),
                      const SizedBox(height: 8),
                    ],
                  ),
                ),
    );
  }
}
