import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:flutter_hospital_management/core/pdf/document_builders.dart';
import 'package:flutter_hospital_management/core/pdf/pdf_export.dart';
import 'package:flutter_hospital_management/models/patient.dart';
import 'package:flutter_hospital_management/providers/patient_provider.dart';
import 'package:flutter_hospital_management/providers/appointment_provider.dart';
import 'package:flutter_hospital_management/providers/admission_provider.dart';
import 'package:flutter_hospital_management/providers/billing_provider.dart';
import 'package:flutter_hospital_management/providers/prescription_provider.dart';
import 'package:flutter_hospital_management/screens/patient_form_screen.dart';
import 'package:flutter_hospital_management/screens/appointment_detail_screen.dart';
import 'package:flutter_hospital_management/screens/admission_detail_screen.dart';
import 'package:flutter_hospital_management/screens/billing_invoice_detail_screen.dart';
import 'package:flutter_hospital_management/screens/prescription_detail_screen.dart';
import 'package:flutter_hospital_management/widgets/app_drawer.dart';
import 'package:flutter_hospital_management/widgets/common.dart';
import 'package:flutter_hospital_management/theme.dart';

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
    _loadRelated();
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

  void _loadRelated() {
    ref.read(appointmentNotifierProvider.notifier).load();
    ref.read(admissionNotifierProvider.notifier).load();
    ref.read(billingNotifierProvider.notifier).load();
    ref.read(prescriptionNotifierProvider.notifier).loadAll();
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
            child: const Text('Delete', style: TextStyle(color: Colors.red)),
          ),
        ],
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    final p = _patient;
    return DefaultTabController(
      length: 5,
      child: Scaffold(
        appBar: AppBar(
          title: Text(p?.name ?? 'Patient'),
          bottom: const TabBar(
            isScrollable: true,
            tabs: [
              Tab(text: 'Overview'),
              Tab(text: 'Appointments'),
              Tab(text: 'Admissions'),
              Tab(text: 'Bills'),
              Tab(text: 'Prescriptions'),
            ],
          ),
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
              IconButton(
                icon: const Icon(Icons.picture_as_pdf),
                tooltip: 'Download Summary PDF',
                onPressed: () => _downloadSummary(p!),
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
                : TabBarView(
                    children: [
                      _overview(p!),
                      _appointmentsTab(p),
                      _admissionsTab(p),
                      _billsTab(p),
                      _prescriptionsTab(p),
                    ],
                  ),
      ),
    );
  }

  Widget _overview(Patient p) => RefreshIndicator(
        onRefresh: _load,
        child: ListView(
          padding: const EdgeInsets.all(16),
          children: [
            Center(
              child: Column(
                children: [
                  CircleAvatar(
                    radius: 38,
                    backgroundColor: AppTheme.primary.withValues(alpha: 0.12),
                    child:
                        const Icon(Icons.person, size: 40, color: AppTheme.primary),
                  ),
                  const SizedBox(height: 10),
                  Text(p.name,
                      style: const TextStyle(
                          fontSize: 20, fontWeight: FontWeight.bold)),
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
              DetailRow('Patient Code', p.patientCode, icon: Icons.badge),
              DetailRow('Date of Birth', p.dateOfBirth, icon: Icons.cake),
              DetailRow('Gender', p.gender, icon: Icons.wc),
              DetailRow('Blood Group', p.bloodGroup, icon: Icons.bloodtype),
              DetailRow('Marital Status', p.maritalStatus, icon: Icons.favorite),
              DetailRow('National ID', p.nationalId, icon: Icons.credit_card),
            ], icon: Icons.person),
            DetailSection('Contact', [
              DetailRow('Phone', p.phone, icon: Icons.phone),
              DetailRow('Alternate Phone', p.alternatePhone,
                  icon: Icons.phone_android),
              DetailRow('Email', p.email, icon: Icons.email),
              DetailRow('Address', p.address, icon: Icons.home),
              DetailRow('City', p.city, icon: Icons.location_city),
              DetailRow('District', p.district, icon: Icons.map),
              DetailRow('Postal Code', p.postalCode, icon: Icons.local_post_office),
            ], icon: Icons.contact_phone),
            DetailSection('Emergency Contact', [
              DetailRow('Name', p.emergencyContactName, icon: Icons.person_pin),
              DetailRow('Number', p.emergencyContactNumber, icon: Icons.phone),
              DetailRow('Relationship', p.relationship, icon: Icons.people),
            ], icon: Icons.emergency),
            const SizedBox(height: 8),
          ],
        ),
      );

  Widget _relatedList<T>({
    required List<T> items,
    required Widget Function(T) tile,
    required String empty,
  }) {
    if (items.isEmpty) {
      return Center(
          child: Padding(
              padding: const EdgeInsets.all(24),
              child: Text(empty, style: const TextStyle(color: Colors.grey))));
    }
    return ListView.separated(
      padding: const EdgeInsets.all(16),
      itemCount: items.length,
      separatorBuilder: (_, _) => const SizedBox(height: 10),
      itemBuilder: (_, i) => tile(items[i]),
    );
  }

  Widget _appointmentsTab(Patient p) {
    final list = ref
        .watch(appointmentNotifierProvider)
        .appointments
        .where((a) => a.registeredPatientId == p.id)
        .toList();
    return _relatedList(
      items: list,
      empty: 'No appointments for this patient.',
      tile: (a) => AppCard(
        onTap: () => Navigator.push(
          context,
          MaterialPageRoute(
              builder: (_) => AppointmentDetailScreen(appointment: a)),
        ),
        child: ListTile(
          leading: const Icon(Icons.calendar_today, color: AppTheme.primary),
          title: Text(a.appointmentNumber ?? 'Appointment'),
          subtitle: Text('${a.appointmentDate ?? ''}  •  ${a.status ?? ''}'),
        ),
      ),
    );
  }

  Widget _admissionsTab(Patient p) {
    final list = ref
        .watch(admissionNotifierProvider)
        .admissions
        .where((a) => a.patientId == p.id)
        .toList();
    return _relatedList(
      items: list,
      empty: 'No admissions for this patient.',
      tile: (a) => AppCard(
        onTap: () => Navigator.push(
          context,
          MaterialPageRoute(builder: (_) => AdmissionDetailScreen(item: a)),
        ),
        child: ListTile(
          leading: const Icon(Icons.assignment_ind, color: AppTheme.primary),
          title: Text('Admission #${a.admissionId ?? ''}'),
          subtitle: Text('${a.admissionDate ?? ''}  •  ${a.status ?? ''}'),
        ),
      ),
    );
  }

  Widget _billsTab(Patient p) {
    final list = ref
        .watch(billingNotifierProvider)
        .invoices
        .where((b) => b.patientId == p.id)
        .toList();
    return _relatedList(
      items: list,
      empty: 'No bills for this patient.',
      tile: (b) => AppCard(
        onTap: () => Navigator.push(
          context,
          MaterialPageRoute(
              builder: (_) => BillingInvoiceDetailScreen(invoiceId: b.id!)),
        ),
        child: ListTile(
          leading: const Icon(Icons.receipt_long, color: AppTheme.primary),
          title: Text(b.invoiceNumber ?? 'Invoice'),
          subtitle: Text(
              '৳ ${b.netAmount ?? 0}  •  ${b.invoiceStatus ?? ''}'),
        ),
      ),
    );
  }

  Widget _prescriptionsTab(Patient p) {
    final list = ref
        .watch(prescriptionNotifierProvider)
        .prescriptions
        .where((rx) => rx.patientName == p.name)
        .toList();
    return _relatedList(
      items: list,
      empty: 'No prescriptions for this patient.',
      tile: (rx) => AppCard(
        onTap: () => Navigator.push(
          context,
          MaterialPageRoute(
              builder: (_) => PrescriptionDetailScreen(id: rx.id!)),
        ),
        child: ListTile(
          leading: const Icon(Icons.medication, color: AppTheme.primary),
          title: Text(rx.prescriptionNumber ?? 'Prescription'),
          subtitle: Text('${rx.createdDate ?? ''}  •  ${rx.doctorName ?? ''}'),
        ),
      ),
    );
  }
}
