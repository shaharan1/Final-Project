import 'dart:io';
import 'package:flutter_hospital_management/core/pdf/document_builders.dart';
import 'package:flutter_hospital_management/models/patient.dart';
import 'package:flutter_hospital_management/models/prescription.dart';
import 'package:flutter_hospital_management/models/billing_invoice.dart';
import 'package:flutter_hospital_management/models/lab_report.dart';
import 'package:flutter_hospital_management/models/appointment.dart';
import 'package:flutter_hospital_management/models/admission.dart';

void main() async {
  final rx = PrescriptionResponse(
    id: 1,
    prescriptionNumber: 'RX-1',
    patientName: 'John Doe',
    doctorName: 'Dr. Smith',
    diagnosis: 'Fever',
    prescriptionItems: [
      PrescriptionItemResponse(
          medicineName: 'Paracetamol',
          dosage: '500mg',
          duration: '5 days',
          instruction: 'After meal')
    ],
  );
  final inv = BillingInvoice(
    id: 1,
    invoiceNumber: 'INV-1',
    patientName: 'John Doe',
    items: [
      BillingInvoiceItem(
          categoryName: 'Consultation',
          quantity: 1,
          unitPrice: 500,
          amount: 500)
    ],
    subtotal: 500,
    taxAmount: 50,
    netAmount: 550,
    totalPaid: 550,
    dueAmount: 0,
  );
  final lab = LabReport(
    id: 1,
    reportNumber: 'LAB-1',
    testName: 'CBC',
    patientName: 'John Doe',
    results: [
      LabReportResult(
          parameterName: 'Hb',
          resultValue: '13',
          unit: 'g/dL',
          statusLabel: 'Normal')
    ],
  );
  final pat = Patient(name: 'John Doe', patientCode: 'P1', gender: 'Male');

  final rxB = await savePdf(buildPrescriptionPdf(rx));
  File('build/rx.pdf').createSync(recursive: true);
  File('build/rx.pdf').writeAsBytesSync(rxB);
  final invB = await savePdf(buildInvoicePdf(inv));
  File('build/inv.pdf').writeAsBytesSync(invB);
  final labB = await savePdf(buildLabReportPdf(lab));
  File('build/lab.pdf').writeAsBytesSync(labB);
  final patB = await savePdf(buildPatientPdf(pat, <Appointment>[], <Admission>[],
      <BillingInvoice>[], <PrescriptionResponse>[]));
  File('build/pat.pdf').writeAsBytesSync(patB);
  print(
      'OK sizes: rx=${rxB.length} inv=${invB.length} lab=${labB.length} pat=${patB.length}');
}
