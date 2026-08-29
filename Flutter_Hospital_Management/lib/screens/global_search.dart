import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:flutter_hospital_management/providers/patient_provider.dart';
import 'package:flutter_hospital_management/providers/appointment_provider.dart';
import 'package:flutter_hospital_management/providers/pharmacy_provider.dart';
import 'package:flutter_hospital_management/providers/billing_provider.dart';
import 'package:flutter_hospital_management/screens/patient_detail_screen.dart';
import 'package:flutter_hospital_management/screens/medicine_detail_screen.dart';
import 'package:flutter_hospital_management/screens/appointment_detail_screen.dart';
import 'package:flutter_hospital_management/screens/billing_invoice_detail_screen.dart';

class AppSearchDelegate extends SearchDelegate<String> {
  @override
  List<Widget>? buildActions(BuildContext context) => [
        if (query.isNotEmpty)
          IconButton(
            icon: const Icon(Icons.clear),
            tooltip: 'Clear',
            onPressed: () => query = '',
          ),
      ];

  @override
  Widget buildLeading(BuildContext context) => IconButton(
        icon: const Icon(Icons.arrow_back),
        tooltip: 'Back',
        onPressed: () => close(context, ''),
      );

  @override
  Widget buildResults(BuildContext context) => _body();

  @override
  Widget buildSuggestions(BuildContext context) => _body();

  Widget _body() {
    final q = query.trim().toLowerCase();
    return Consumer(
      builder: (context, ref, _) {
        if (q.length < 2) {
          return const Center(
              child: Text('Type at least 2 characters to search.'));
        }

        final patients = ref
            .watch(patientNotifierProvider)
            .patients
            .where((p) => (p.name ?? '').toLowerCase().contains(q))
            .toList();
        final medicines = ref
            .watch(medicineNotifierProvider)
            .medicines
            .where((m) => m.medicineName.toLowerCase().contains(q))
            .toList();
        final appointments = ref
            .watch(appointmentNotifierProvider)
            .appointments
            .where((a) =>
                (a.appointmentNumber ?? '').toLowerCase().contains(q) ||
                (a.patientName ?? '').toLowerCase().contains(q))
            .toList();
        final invoices = ref
            .watch(billingNotifierProvider)
            .invoices
            .where((b) => (b.invoiceNumber ?? '').toLowerCase().contains(q))
            .toList();

        final tiles = <Widget>[];
        for (final p in patients) {
          tiles.add(_row(context, Icons.people, p.name,
              p.patientCode ?? 'Patient', () {
            close(context, '');
            Navigator.push(
              context,
              MaterialPageRoute(
                  builder: (_) => PatientDetailScreen(patientId: p.id!)),
            );
          }));
        }
        for (final m in medicines) {
          tiles.add(_row(context, Icons.medication, m.medicineName, 'Medicine',
              () {
            close(context, '');
            Navigator.push(
              context,
              MaterialPageRoute(builder: (_) => MedicineDetailScreen(item: m)),
            );
          }));
        }
        for (final a in appointments) {
          tiles.add(_row(context, Icons.calendar_today,
              a.appointmentNumber ?? 'Appointment', a.patientName ?? '', () {
            close(context, '');
            Navigator.push(
              context,
              MaterialPageRoute(
                  builder: (_) => AppointmentDetailScreen(appointment: a)),
            );
          }));
        }
        for (final b in invoices) {
          tiles.add(_row(context, Icons.receipt_long,
              b.invoiceNumber ?? 'Invoice', '৳ ${b.netAmount ?? 0}', () {
            close(context, '');
            Navigator.push(
              context,
              MaterialPageRoute(
                  builder: (_) =>
                      BillingInvoiceDetailScreen(invoiceId: b.id!)),
            );
          }));
        }

        if (tiles.isEmpty) {
          return const Center(child: Text('No results found.'));
        }
        return ListView(children: tiles);
      },
    );
  }

  Widget _row(BuildContext context, IconData icon, String title, String sub,
      VoidCallback onTap) {
    return ListTile(
      leading: Icon(icon, color: Theme.of(context).primaryColor),
      title: Text(title),
      subtitle: sub.isNotEmpty ? Text(sub) : null,
      onTap: onTap,
    );
  }
}
