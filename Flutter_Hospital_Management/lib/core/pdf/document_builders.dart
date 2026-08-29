import 'dart:typed_data';

import 'package:pdf/pdf.dart';
import 'package:pdf/widgets.dart' as pw;
import 'package:flutter_hospital_management/models/admission.dart';
import 'package:flutter_hospital_management/models/appointment.dart';
import 'package:flutter_hospital_management/models/billing_invoice.dart';
import 'package:flutter_hospital_management/models/lab_report.dart';
import 'package:flutter_hospital_management/models/patient.dart';
import 'package:flutter_hospital_management/models/prescription.dart';

const String _hospital = 'Elite Care Hospital';

String _s(String? v) => (v == null || v.isEmpty) ? '-' : v;
String _money(double? v) => 'TK ${(v ?? 0).toStringAsFixed(2)}';

pw.Widget _title(String t) => pw.Container(
      padding: const pw.EdgeInsets.only(bottom: 8),
      decoration: const pw.BoxDecoration(
        border: pw.Border(
          bottom: pw.BorderSide(width: 1, color: PdfColors.grey),
        ),
      ),
      child: pw.Column(
        crossAxisAlignment: pw.CrossAxisAlignment.start,
        children: [
          pw.Text(_hospital,
              style: pw.TextStyle(bold: true, fontSize: 20)),
          pw.SizedBox(height: 2),
          pw.Text(t,
              style: pw.TextStyle(fontSize: 14, color: PdfColors.grey700)),
        ],
      ),
    );

pw.Widget _kv(String k, String? v) => pw.Padding(
      padding: const pw.EdgeInsets.symmetric(vertical: 2),
      child: pw.Row(
        crossAxisAlignment: pw.CrossAxisAlignment.start,
        children: [
          pw.SizedBox(
            width: 130,
            child: pw.Text(k,
                style: const pw.TextStyle(color: PdfColors.grey700)),
          ),
          pw.Expanded(child: pw.Text(_s(v))),
        ],
      ),
    );

pw.Widget _sectionTitle(String t) => pw.Padding(
      padding: const pw.EdgeInsets.only(top: 12, bottom: 4),
      child: pw.Text(t, style: pw.TextStyle(bold: true, fontSize: 13)),
    );

pw.Widget _table(List<String> headers, List<List<String?>> rows) {
  if (rows.isEmpty) {
    return pw.Text('No records.',
        style: const pw.TextStyle(color: PdfColors.grey700, italic: true));
  }
  return pw.Table(
    border: pw.TableBorder.all(width: 0.5, color: PdfColors.grey),
    children: [
      pw.TableRow(
        decoration: const pw.BoxDecoration(color: PdfColors.grey200),
        children: headers
            .map((h) => pw.Padding(
                  padding: const pw.EdgeInsets.all(4),
                  child: pw.Text(h, style: pw.TextStyle(bold: true)),
                ))
            .toList(),
      ),
      ...rows.map(
        (r) => pw.TableRow(
          children: r
              .map((v) => pw.Padding(
                    padding: const pw.EdgeInsets.all(4),
                    child: pw.Text(_s(v)),
                  ))
              .toList(),
        ),
      ),
    ],
  );
}

Future<Uint8List> savePdf(pw.Document doc) => doc.save();

pw.Document buildPrescriptionPdf(PrescriptionResponse rx) {
  final doc = pw.Document(title: 'Prescription');
  doc.addPage(
    pw.Page(
      pageFormat: PdfPageFormat.a4,
      margin: const pw.EdgeInsets.all(32),
      build: (c) => pw.Column(
        crossAxisAlignment: pw.CrossAxisAlignment.start,
        children: [
          _title('PRESCRIPTION'),
          pw.SizedBox(height: 8),
          _kv('Prescription No', rx.prescriptionNumber),
          _kv('Date', rx.createdDate),
          _kv('Patient', rx.patientName),
          _kv('Doctor', rx.doctorName),
          _kv('Diagnosis', rx.diagnosis),
          _kv('Chief Complaints', rx.chiefComplaints),
          _kv('Symptoms', rx.symptoms),
          _kv('Next Follow-up', rx.nextFollowUpDate),
          _kv('Notes', rx.notes),
          _kv('Dispensed', rx.dispensed == true ? 'Yes' : 'No'),
          _sectionTitle('Medicines'),
          _table(
            ['Medicine', 'Dosage', 'Duration', 'Instruction'],
            rx.prescriptionItems
                .map((it) => [
                      it.medicineName,
                      it.dosage,
                      it.duration,
                      it.instruction,
                    ])
                .toList(),
          ),
        ],
      ),
    ),
  );
  return doc;
}

pw.Document buildInvoicePdf(BillingInvoice inv) {
  final doc = pw.Document(title: 'Invoice');
  doc.addPage(
    pw.Page(
      pageFormat: PdfPageFormat.a4,
      margin: const pw.EdgeInsets.all(32),
      build: (c) => pw.Column(
        crossAxisAlignment: pw.CrossAxisAlignment.start,
        children: [
          _title('INVOICE'),
          pw.SizedBox(height: 8),
          _kv('Invoice No', inv.invoiceNumber),
          _kv('Patient', inv.patientName),
          _kv('Patient Code', inv.patientCode),
          _kv('Phone', inv.patientPhone),
          _kv('Type', inv.invoiceType),
          _kv('Payment Status', inv.paymentStatus),
          _kv('Invoice Status', inv.invoiceStatus),
          _kv('Prepared By', inv.preparedBy),
          _sectionTitle('Items'),
          _table(
            ['Item', 'Qty', 'Unit', 'Amount'],
            inv.items
                .map((it) => [
                      it.categoryName ?? it.description ?? 'Item',
                      it.quantity?.toString(),
                      it.unitPrice?.toStringAsFixed(2),
                      it.amount?.toStringAsFixed(2),
                    ])
                .toList(),
          ),
          _sectionTitle('Summary'),
          _kv('Subtotal', _money(inv.subtotal)),
          _kv('Tax (${_s(inv.taxRate?.toString())}%)', _money(inv.taxAmount)),
          _kv('Discount', _money(inv.discountAmount)),
          _kv('Net Amount', _money(inv.netAmount)),
          _kv('Total Paid', _money(inv.totalPaid)),
          _kv('Due', _money(inv.dueAmount)),
        ],
      ),
    ),
  );
  return doc;
}

pw.Document buildLabReportPdf(LabReport r) {
  final doc = pw.Document(title: 'Lab Report');
  doc.addPage(
    pw.Page(
      pageFormat: PdfPageFormat.a4,
      margin: const pw.EdgeInsets.all(32),
      build: (c) => pw.Column(
        crossAxisAlignment: pw.CrossAxisAlignment.start,
        children: [
          _title('LABORATORY REPORT'),
          pw.SizedBox(height: 8),
          _kv('Report No', r.reportNumber),
          _kv('Test', r.testName),
          _kv('Patient', r.patientName),
          _kv('Code', r.patientCode),
          _kv('Doctor', r.doctorName),
          _kv('Specialist', r.specialistName),
          _kv('Status', r.statusLabel ?? r.reportStatus),
          _kv('Reported', r.reportedDate),
          _sectionTitle('Results'),
          _table(
            ['Parameter', 'Value', 'Reference', 'Status'],
            r.results
                .map((res) => [
                      res.parameterName,
                      '${res.resultValue ?? '-'} ${res.unit ?? ''}',
                      res.referenceRangeDisplay,
                      res.statusLabel,
                    ])
                .toList(),
          ),
          if (r.finalImpression != null) ...[
            _sectionTitle('Impression'),
            pw.Text(r.finalImpression!),
          ],
          if (r.recommendation != null) ...[
            _sectionTitle('Recommendation'),
            pw.Text(r.recommendation!),
          ],
        ],
      ),
    ),
  );
  return doc;
}

pw.Document buildPatientPdf(
  Patient p,
  List<Appointment> appointments,
  List<Admission> admissions,
  List<BillingInvoice> invoices,
  List<PrescriptionResponse> prescriptions,
) {
  final doc = pw.Document(title: 'Patient Summary');
  doc.addPage(
    pw.Page(
      pageFormat: PdfPageFormat.a4,
      margin: const pw.EdgeInsets.all(32),
      build: (c) => pw.Column(
        crossAxisAlignment: pw.CrossAxisAlignment.start,
        children: [
          _title('PATIENT SUMMARY'),
          pw.SizedBox(height: 8),
          _kv('Patient Code', p.patientCode),
          _kv('Name', p.name),
          _kv('Gender', p.gender),
          _kv('Date of Birth', p.dateOfBirth),
          _kv('Blood Group', p.bloodGroup),
          _kv('Phone', p.phone),
          _kv('Email', p.email),
          _kv('Address', p.address == null
              ? null
              : [p.address, p.city, p.district, p.postalCode]
                  .where((e) => e != null && e.isNotEmpty)
                  .join(', ')),
          _kv('Emergency Contact',
              '${p.emergencyContactName ?? ''} (${p.relationship ?? ''}) - ${p.emergencyContactNumber ?? ''}'),
          _sectionTitle('Appointments'),
          _table(
            ['No', 'Date', 'Status'],
            appointments
                .map((a) => [
                      a.appointmentNumber,
                      a.appointmentDate,
                      a.status,
                    ])
                .toList(),
          ),
          _sectionTitle('Admissions'),
          _table(
            ['Adm. ID', 'Date', 'Status'],
            admissions
                .map((a) => [
                      a.admissionId?.toString(),
                      a.admissionDate,
                      a.status,
                    ])
                .toList(),
          ),
          _sectionTitle('Bills'),
          _table(
            ['Invoice', 'Net Amount', 'Status'],
            invoices
                .map((b) => [
                      b.invoiceNumber,
                      _money(b.netAmount),
                      b.invoiceStatus,
                    ])
                .toList(),
          ),
          _sectionTitle('Prescriptions'),
          _table(
            ['Rx No', 'Date', 'Doctor'],
            prescriptions
                .map((rx) => [
                      rx.prescriptionNumber,
                      rx.createdDate,
                      rx.doctorName,
                    ])
                .toList(),
          ),
        ],
      ),
    ),
  );
  return doc;
}
