import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:flutter_hospital_management/models/bed.dart';
import 'package:flutter_hospital_management/models/doctor.dart';
import 'package:flutter_hospital_management/models/patient.dart';
import 'package:flutter_hospital_management/models/admission.dart';
import 'package:flutter_hospital_management/providers/admission_provider.dart';
import 'package:flutter_hospital_management/providers/infrastructure_provider.dart';
import 'package:flutter_hospital_management/providers/patient_provider.dart';
import 'package:flutter_hospital_management/providers/doctor_provider.dart';

class AdmissionFormScreen extends ConsumerStatefulWidget {
  const AdmissionFormScreen({super.key});

  @override
  ConsumerState<AdmissionFormScreen> createState() =>
      _AdmissionFormScreenState();
}

class _AdmissionFormScreenState extends ConsumerState<AdmissionFormScreen> {
  final _formKey = GlobalKey<FormState>();
  Patient? _patient;
  Doctor? _doctor;
  Bed? _bed;
  final _diagnosis = TextEditingController();
  bool _saving = false;

  @override
  void initState() {
    super.initState();
    Future.microtask(() {
      ref.read(patientNotifierProvider.notifier).load();
      ref.read(doctorNotifierProvider.notifier).load();
      ref.read(bedNotifierProvider.notifier).load();
    });
  }

  @override
  void dispose() {
    _diagnosis.dispose();
    super.dispose();
  }

  Future<void> _submit() async {
    if (!_formKey.currentState!.validate()) return;
    if (_patient == null || _doctor == null || _bed == null) {
      _snack('Patient, Doctor and an Available Bed are required.');
      return;
    }

    final req = AdmissionRequest(
      patientId: _patient!.id,
      doctorId: _doctor!.id,
      bedId: _bed!.id,
      initialDiagnosis: _diagnosis.text.trim(),
    );

    setState(() => _saving = true);
    final ok = await ref.read(admissionNotifierProvider.notifier).admit(req);
    setState(() => _saving = false);
    if (ok && mounted) Navigator.pop(context);
  }

  void _snack(String m) => ScaffoldMessenger.of(context)
      .showSnackBar(SnackBar(content: Text(m), backgroundColor: Colors.red));

  @override
  Widget build(BuildContext context) {
    final patients = ref.watch(patientNotifierProvider).patients;
    final doctors = ref.watch(doctorNotifierProvider).doctors;
    final beds = ref
        .watch(bedNotifierProvider)
        .beds
        .where((b) => (b.status ?? '').toUpperCase() == 'AVAILABLE')
        .toList();

    return Scaffold(
      appBar: AppBar(
        title: const Text('Admit Patient'),
        backgroundColor: Colors.teal,
        foregroundColor: Colors.white,
      ),
      body: SingleChildScrollView(
        padding: const EdgeInsets.all(16),
        child: Form(
          key: _formKey,
          child: Column(
            children: [
              DropdownButtonFormField<Patient>(
                initialValue: _patient,
                decoration: const InputDecoration(labelText: 'Patient *', border: OutlineInputBorder()),
                items: patients
                    .map((p) => DropdownMenuItem(
                        value: p,
                        child: Text('${p.name} (${p.patientCode ?? p.phone ?? p.id})')))
                    .toList(),
                onChanged: (v) => setState(() => _patient = v),
                validator: (v) => v == null ? 'Required' : null,
              ),
              const SizedBox(height: 12),
              DropdownButtonFormField<Doctor>(
                initialValue: _doctor,
                decoration: const InputDecoration(labelText: 'Doctor *', border: OutlineInputBorder()),
                items: doctors
                    .map((d) => DropdownMenuItem(value: d, child: Text(d.displayName)))
                    .toList(),
                onChanged: (v) => setState(() => _doctor = v),
                validator: (v) => v == null ? 'Required' : null,
              ),
              const SizedBox(height: 12),
              DropdownButtonFormField<Bed>(
                initialValue: _bed,
                decoration: const InputDecoration(labelText: 'Available Bed *', border: OutlineInputBorder()),
                items: beds
                    .map((b) => DropdownMenuItem(
                        value: b,
                        child: Text('Bed ${b.bedNumber} — ${b.wardName ?? ''} (${b.roomType ?? ''})')))
                    .toList(),
                onChanged: (v) => setState(() => _bed = v),
                validator: (v) => v == null ? 'No available bed / required' : null,
              ),
              const SizedBox(height: 12),
              TextFormField(
                controller: _diagnosis,
                decoration: const InputDecoration(labelText: 'Initial Diagnosis', border: OutlineInputBorder()),
                maxLines: 2,
              ),
              const SizedBox(height: 20),
              SizedBox(
                width: double.infinity,
                child: ElevatedButton(
                  style: ElevatedButton.styleFrom(
                    backgroundColor: Colors.teal,
                    foregroundColor: Colors.white,
                    padding: const EdgeInsets.symmetric(vertical: 14),
                  ),
                  onPressed: _saving ? null : _submit,
                  child: _saving
                      ? const SizedBox(height: 18, width: 18, child: CircularProgressIndicator(strokeWidth: 2, color: Colors.white))
                      : const Text('Admit'),
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
