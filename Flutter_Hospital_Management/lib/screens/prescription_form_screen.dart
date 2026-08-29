import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:flutter_hospital_management/models/appointment.dart';
import 'package:flutter_hospital_management/models/medicine.dart';
import 'package:flutter_hospital_management/models/patient.dart';
import 'package:flutter_hospital_management/models/prescription.dart';
import 'package:flutter_hospital_management/models/test_master.dart';
import 'package:flutter_hospital_management/providers/appointment_provider.dart';
import 'package:flutter_hospital_management/providers/patient_provider.dart';
import 'package:flutter_hospital_management/providers/pharmacy_provider.dart';
import 'package:flutter_hospital_management/providers/lab_provider.dart';
import 'package:flutter_hospital_management/providers/prescription_provider.dart';
import 'package:flutter_hospital_management/widgets/common.dart';
import 'package:flutter_hospital_management/theme.dart';

class PrescriptionFormScreen extends ConsumerStatefulWidget {
  final int doctorId;
  final int? patientId;
  final int? appointmentId;

  const PrescriptionFormScreen({
    super.key,
    required this.doctorId,
    this.patientId,
    this.appointmentId,
  });

  @override
  ConsumerState<PrescriptionFormScreen> createState() =>
      _PrescriptionFormScreenState();
}

class _PrescriptionFormScreenState
    extends ConsumerState<PrescriptionFormScreen> {
  final _formKey = GlobalKey<FormState>();
  List<Patient> _patients = [];
  List<Medicine> _medicines = [];
  List<Appointment> _appointments = [];
  List<TestMaster> _tests = [];

  final Set<int> _selectedTestIds = {};
  int? _selectedPatientId;
  int? _selectedAppointmentId;
  final _diagnosis = TextEditingController();
  final _complaints = TextEditingController();
  final _symptoms = TextEditingController();
  final _bp = TextEditingController();
  final _pulse = TextEditingController();
  final _temp = TextEditingController();
  final _weight = TextEditingController();
  final _notes = TextEditingController();
  DateTime? _followUp;

  final List<_MedicineRow> _rows = [];
  bool _submitting = false;

  @override
  void initState() {
    super.initState();
    _selectedPatientId = widget.patientId;
    _selectedAppointmentId = widget.appointmentId;
    _load();
    _addRow();
  }

  Future<void> _load() async {
    try {
      final patients =
          await ref.read(patientServiceProvider).getAll();
      final medicines =
          await ref.read(pharmacyServiceProvider).getMedicines();
      final appointments = await ref
          .read(appointmentServiceProvider)
          .getByDoctor(widget.doctorId);
      final tests = await ref.read(labServiceProvider).getTestMasters();
      if (mounted) {
        setState(() {
          _patients = patients;
          _medicines = medicines;
          _appointments = appointments;
          _tests = tests;
        });
      }
    } catch (e) {
      if (mounted) {
        ScaffoldMessenger.of(context)
            .showSnackBar(SnackBar(content: Text('Load error: $e')));
      }
    }
  }

  void _addRow() => setState(() => _rows.add(_MedicineRow(medicines: _medicines)));

  String _fmtDate(DateTime d) =>
      '${d.year}-${d.month.toString().padLeft(2, '0')}-${d.day.toString().padLeft(2, '0')}';

  Future<void> _submit() async {
    if (!_formKey.currentState!.validate()) return;
    if (_selectedPatientId == null) {
      ScaffoldMessenger.of(context)
          .showSnackBar(const SnackBar(content: Text('Select a patient.')));
      return;
    }
    final items = _rows
        .where((r) => r.medicineId != null)
        .map((r) => PrescriptionItemRequest(
              medicineId: r.medicineId,
              dosage: r.dosage.text.trim(),
              duration: r.duration.text.trim(),
              instruction: r.instruction.text.trim(),
            ))
        .toList();

    final request = PrescriptionRequest(
      doctorId: widget.doctorId,
      patientId: _selectedPatientId,
      appointmentId: _selectedAppointmentId,
      diagnosis: _diagnosis.text.trim(),
      chiefComplaints: _complaints.text.trim(),
      symptoms: _symptoms.text.trim(),
      bloodPressure: _bp.text.trim(),
      pulseRate: _pulse.text.trim(),
      bodyTemperature: _temp.text.trim(),
      weight: _weight.text.trim(),
      notes: _notes.text.trim(),
      nextFollowUpDate: _followUp != null ? _fmtDate(_followUp!) : null,
      prescriptionItems: items,
      testIds: _selectedTestIds.isEmpty ? null : _selectedTestIds.toList(),
    );

    setState(() => _submitting = true);
    final ok = await ref
        .read(prescriptionNotifierProvider.notifier)
        .create(request);
    setState(() => _submitting = false);

    if (!mounted) return;
    if (ok) {
      ScaffoldMessenger.of(context)
          .showSnackBar(const SnackBar(content: Text('Prescription created.')));
      Navigator.pop(context);
    } else {
      final err = ref.read(prescriptionNotifierProvider).error;
      ScaffoldMessenger.of(context)
          .showSnackBar(SnackBar(content: Text('Failed: $err')));
    }
  }

  @override
  void dispose() {
    _diagnosis.dispose();
    _complaints.dispose();
    _symptoms.dispose();
    _bp.dispose();
    _pulse.dispose();
    _temp.dispose();
    _weight.dispose();
    _notes.dispose();
    for (final r in _rows) {
      r.dispose();
    }
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('New Prescription')),
      body: SingleChildScrollView(
        padding: const EdgeInsets.all(16),
        child: Form(
          key: _formKey,
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              _section('Patient & Visit', [
                Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    const Text('Patient *',
                        style: TextStyle(fontSize: 12, color: Colors.grey)),
                    DropdownButton<int>(
                      isExpanded: true,
                      value: _selectedPatientId,
                      hint: const Text('Select patient'),
                      items: _patients
                          .map((p) => DropdownMenuItem(
                                value: p.id,
                                child: Text(p.name),
                              ))
                          .toList(),
                      onChanged: (v) =>
                          setState(() => _selectedPatientId = v),
                    ),
                  ],
                ),
                const SizedBox(height: 12),
                Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    const Text('Appointment (optional)',
                        style: TextStyle(fontSize: 12, color: Colors.grey)),
                    DropdownButton<int>(
                      isExpanded: true,
                      value: _selectedAppointmentId,
                      hint: const Text('None'),
                      items: _appointments
                          .map((a) => DropdownMenuItem(
                                value: a.id,
                                child: Text(
                                    '${a.appointmentNumber ?? a.id} • ${a.appointmentDate ?? ''}'),
                              ))
                          .toList(),
                      onChanged: (v) =>
                          setState(() => _selectedAppointmentId = v),
                    ),
                  ],
                ),
              ]),
              _section('Clinical Notes', [
                TextFormField(
                  controller: _diagnosis,
                  decoration: const InputDecoration(labelText: 'Diagnosis'),
                ),
                const SizedBox(height: 12),
                TextFormField(
                  controller: _complaints,
                  decoration:
                      const InputDecoration(labelText: 'Chief Complaints'),
                ),
                const SizedBox(height: 12),
                TextFormField(
                  controller: _symptoms,
                  decoration: const InputDecoration(labelText: 'Symptoms'),
                ),
              ]),
              _section('Vitals', [
                Row(
                  children: [
                    Expanded(
                        child: TextFormField(
                            controller: _bp,
                            decoration:
                                const InputDecoration(labelText: 'Blood Pressure'))),
                    const SizedBox(width: 12),
                    Expanded(
                        child: TextFormField(
                            controller: _pulse,
                            decoration:
                                const InputDecoration(labelText: 'Pulse'))),
                  ],
                ),
                const SizedBox(height: 12),
                Row(
                  children: [
                    Expanded(
                        child: TextFormField(
                            controller: _temp,
                            decoration: const InputDecoration(
                                labelText: 'Temperature'))),
                    const SizedBox(width: 12),
                    Expanded(
                        child: TextFormField(
                            controller: _weight,
                            decoration:
                                const InputDecoration(labelText: 'Weight'))),
                  ],
                ),
              ]),
              _section('Medicines', [
                ..._rows.map((r) => _rowCard(r)),
                const SizedBox(height: 8),
                OutlinedButton.icon(
                  onPressed: _addRow,
                  icon: const Icon(Icons.add),
                  label: const Text('Add Medicine'),
                ),
              ]),
              _section('Other', [
                TextFormField(
                  controller: _notes,
                  decoration: const InputDecoration(labelText: 'Notes'),
                  maxLines: 2,
                ),
                const SizedBox(height: 12),
                ListTile(
                  contentPadding: EdgeInsets.zero,
                  title: Text(_followUp == null
                      ? 'Next Follow-up: Not set'
                      : 'Next Follow-up: ${_fmtDate(_followUp!)}'),
                  trailing: const Icon(Icons.calendar_today),
                  onTap: () async {
                    final d = await showDatePicker(
                      context: context,
                      initialDate: DateTime.now()
                          .add(const Duration(days: 7)),
                      firstDate: DateTime.now(),
                      lastDate:
                          DateTime.now().add(const Duration(days: 365)),
                    );
                    if (d != null) setState(() => _followUp = d);
                  },
                ),
              ]),
              const SizedBox(height: 20),
              SizedBox(
                width: double.infinity,
                child: ElevatedButton(
                  onPressed: _submitting ? null : _submit,
                  child: _submitting
                      ? const CircularProgressIndicator(color: Colors.white)
                      : const Text('Create Prescription'),
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }

  Widget _section(String title, List<Widget> children) => Padding(
        padding: const EdgeInsets.only(bottom: 16),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            SectionTitle(title, icon: Icons.medical_information),
            AppCard(
              padding: const EdgeInsets.all(14),
              child: Column(children: children),
            ),
          ],
        ),
      );

  Widget _rowCard(_MedicineRow r) => Card(
        margin: const EdgeInsets.only(bottom: 10),
        child: Padding(
          padding: const EdgeInsets.all(12),
          child: Column(
            children: [
              Row(
                children: [
                  Expanded(
                    child: Column(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        const Text('Medicine',
                            style:
                                TextStyle(fontSize: 12, color: Colors.grey)),
                        DropdownButton<int>(
                          isExpanded: true,
                          value: r.medicineId,
                          hint: const Text('Select medicine'),
                          items: _medicines
                              .map((m) => DropdownMenuItem(
                                    value: m.id,
                                    child: Text(m.medicineName),
                                  ))
                              .toList(),
                          onChanged: (v) =>
                              setState(() => r.medicineId = v),
                        ),
                      ],
                    ),
                  ),
                  IconButton(
                    icon: const Icon(Icons.delete, color: AppTheme.danger),
                    onPressed: () => setState(() => _rows.remove(r)),
                  ),
                ],
              ),
              const SizedBox(height: 10),
              Row(
                children: [
                  Expanded(
                      child: TextFormField(
                          controller: r.dosage,
                          decoration:
                              const InputDecoration(labelText: 'Dosage'))),
                  const SizedBox(width: 10),
                  Expanded(
                      child: TextFormField(
                          controller: r.duration,
                          decoration:
                              const InputDecoration(labelText: 'Duration'))),
                ],
              ),
              const SizedBox(height: 10),
              TextFormField(
                controller: r.instruction,
                decoration:
                    const InputDecoration(labelText: 'Instruction'),
              ),
            ],
          ),
        ),
      );
}

class _MedicineRow {
  int? medicineId;
  final TextEditingController dosage = TextEditingController();
  final TextEditingController duration = TextEditingController();
  final TextEditingController instruction = TextEditingController();
  final List<Medicine> medicines;

  _MedicineRow({required this.medicines});

  void dispose() {
    dosage.dispose();
    duration.dispose();
    instruction.dispose();
  }
}
