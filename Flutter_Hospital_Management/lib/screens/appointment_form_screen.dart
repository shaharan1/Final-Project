import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:flutter_hospital_management/models/appointment.dart';
import 'package:flutter_hospital_management/models/doctor.dart';
import 'package:flutter_hospital_management/models/patient.dart';
import 'package:flutter_hospital_management/providers/appointment_provider.dart';
import 'package:flutter_hospital_management/providers/doctor_provider.dart';
import 'package:flutter_hospital_management/providers/patient_provider.dart';

class AppointmentFormScreen extends ConsumerStatefulWidget {
  const AppointmentFormScreen({super.key});

  @override
  ConsumerState<AppointmentFormScreen> createState() =>
      _AppointmentFormScreenState();
}

class _AppointmentFormScreenState extends ConsumerState<AppointmentFormScreen> {
  final _formKey = GlobalKey<FormState>();
  final _name = TextEditingController();
  final _mobile = TextEditingController();
  final _problem = TextEditingController();
  final _transaction = TextEditingController();
  final _date = TextEditingController();
  final _time = TextEditingController();

  Doctor? _doctor;
  Patient? _existingPatient;
  String? _paymentMethod;
  bool _saving = false;

  final _paymentMethods = ['Cash', 'bKash', 'Bank'];

  @override
  void initState() {
    super.initState();
    _paymentMethod = 'Cash';
    Future.microtask(() {
      ref.read(doctorNotifierProvider.notifier).load();
      ref.read(patientNotifierProvider.notifier).load();
    });
  }

  @override
  void dispose() {
    _name.dispose();
    _mobile.dispose();
    _problem.dispose();
    _transaction.dispose();
    _date.dispose();
    _time.dispose();
    super.dispose();
  }

  Future<void> _pickDate() async {
    final now = DateTime.now();
    final picked = await showDatePicker(
      context: context,
      initialDate: now,
      firstDate: now,
      lastDate: now.add(const Duration(days: 365)),
    );
    if (picked != null) {
      _date.text =
          '${picked.year}-${picked.month.toString().padLeft(2, '0')}-${picked.day.toString().padLeft(2, '0')}';
    }
  }

  Future<void> _pickTime() async {
    final picked = await showTimePicker(
      context: context,
      initialTime: const TimeOfDay(hour: 9, minute: 0),
    );
    if (picked != null) {
      _time.text =
          '${picked.hour.toString().padLeft(2, '0')}:${picked.minute.toString().padLeft(2, '0')}:00';
    }
  }

  void _onExistingPatientChanged(Patient? p) {
    setState(() {
      _existingPatient = p;
      if (p != null) {
        _name.text = p.name;
        _mobile.text = p.phone ?? '';
      } else {
        _name.clear();
        _mobile.clear();
      }
    });
  }

  Future<void> _submit() async {
    if (!_formKey.currentState!.validate()) return;
    if (_doctor == null) {
      _showError('Please select a doctor.');
      return;
    }
    if (_date.text.isEmpty || _time.text.isEmpty) {
      _showError('Please pick appointment date and time.');
      return;
    }
    if (_existingPatient == null &&
        (_name.text.trim().isEmpty || _mobile.text.trim().isEmpty)) {
      _showError('Provide patient name & mobile, or pick an existing patient.');
      return;
    }

    setState(() => _saving = true);
    final request = AppointmentRequest(
      patientId: _existingPatient?.id,
      patientName: _name.text.trim(),
      mobileNumber: _mobile.text.trim(),
      specialization: _doctor?.specialization,
      problemDescription: _problem.text.trim(),
      doctorId: _doctor!.id,
      appointmentDate: _date.text,
      appointmentTime: _time.text,
      paymentMethod: _paymentMethod,
      transactionId: _paymentMethod == 'Cash'
          ? null
          : _transaction.text.trim(),
    );
    final ok = await ref
        .read(appointmentNotifierProvider.notifier)
        .book(request);
    setState(() => _saving = false);
    if (ok && mounted) Navigator.pop(context);
  }

  void _showError(String msg) {
    ScaffoldMessenger.of(context)
        .showSnackBar(SnackBar(content: Text(msg), backgroundColor: Colors.red));
  }

  @override
  Widget build(BuildContext context) {
    final doctors = ref.watch(doctorNotifierProvider).doctors;
    final patients = ref.watch(patientNotifierProvider).patients;

    return Scaffold(
      appBar: AppBar(
        title: const Text('Book Appointment'),
        backgroundColor: Colors.teal,
        foregroundColor: Colors.white,
      ),
      body: SingleChildScrollView(
        padding: const EdgeInsets.all(16),
        child: Form(
          key: _formKey,
          child: Column(
            children: [
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
              DropdownButtonFormField<Patient>(
                initialValue: _existingPatient,
                decoration: const InputDecoration(
                  labelText: 'Existing patient (optional)',
                  border: OutlineInputBorder(),
                ),
                hint: const Text('Leave empty for guest'),
                items: patients
                    .map((p) => DropdownMenuItem(
                        value: p,
                        child: Text('${p.name} (${p.patientCode ?? p.phone ?? p.id})')))
                    .toList(),
                onChanged: _onExistingPatientChanged,
              ),
              const SizedBox(height: 12),
              TextFormField(
                controller: _name,
                enabled: _existingPatient == null,
                decoration: const InputDecoration(labelText: 'Patient Name *', border: OutlineInputBorder()),
                validator: (v) => _existingPatient == null && (v == null || v.trim().isEmpty)
                    ? 'Required'
                    : null,
              ),
              const SizedBox(height: 12),
              TextFormField(
                controller: _mobile,
                enabled: _existingPatient == null,
                decoration: const InputDecoration(labelText: 'Mobile Number *', border: OutlineInputBorder()),
                keyboardType: TextInputType.phone,
                validator: (v) => _existingPatient == null && (v == null || v.trim().isEmpty)
                    ? 'Required'
                    : null,
              ),
              const SizedBox(height: 12),
              Row(
                children: [
                  Expanded(
                    child: TextFormField(
                      controller: _date,
                      decoration: const InputDecoration(labelText: 'Date *', border: OutlineInputBorder(), suffixIcon: Icon(Icons.calendar_today)),
                      readOnly: true,
                      onTap: _pickDate,
                    ),
                  ),
                  const SizedBox(width: 12),
                  Expanded(
                    child: TextFormField(
                      controller: _time,
                      decoration: const InputDecoration(labelText: 'Time *', border: OutlineInputBorder(), suffixIcon: Icon(Icons.access_time)),
                      readOnly: true,
                      onTap: _pickTime,
                    ),
                  ),
                ],
              ),
              const SizedBox(height: 12),
              TextFormField(
                controller: _problem,
                decoration: const InputDecoration(labelText: 'Problem Description', border: OutlineInputBorder()),
                maxLines: 2,
              ),
              const SizedBox(height: 12),
              DropdownButtonFormField<String>(
                initialValue: _paymentMethod,
                decoration: const InputDecoration(labelText: 'Payment Method', border: OutlineInputBorder()),
                items: _paymentMethods
                    .map((m) => DropdownMenuItem(value: m, child: Text(m)))
                    .toList(),
                onChanged: (v) => setState(() => _paymentMethod = v),
              ),
              const SizedBox(height: 12),
              if (_paymentMethod != 'Cash')
                TextFormField(
                  controller: _transaction,
                  decoration: const InputDecoration(labelText: 'Transaction ID', border: OutlineInputBorder()),
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
                      : const Text('Book Appointment'),
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
