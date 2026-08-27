import 'package:flutter/material.dart';
import 'package:flutter_hospital_management/models/pharmacy_sale.dart';
import 'package:flutter_hospital_management/widgets/common.dart';

class PharmacySaleDetailScreen extends StatelessWidget {
  final PharmacySale sale;

  const PharmacySaleDetailScreen({super.key, required this.sale});

  String _money(dynamic v) =>
      '৳ ${(v is num ? v.toDouble() : 0).toStringAsFixed(2)}';

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text(sale.saleInvoiceNo ?? 'Sale #${sale.id}')),
      body: ListView(
        padding: const EdgeInsets.all(16),
        children: [
          AppCard(
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                _row('Invoice', sale.saleInvoiceNo ?? '#${sale.id}'),
                _row('Patient', sale.patientName ?? sale.patientType ?? 'Walk-in'),
                if (sale.patientPhone != null) _row('Phone', sale.patientPhone!),
                if (sale.doctorName != null) _row('Doctor', sale.doctorName!),
                _row('Payment', sale.paymentMethod ?? '-'),
                Row(
                  mainAxisAlignment: MainAxisAlignment.spaceBetween,
                  children: [
                    const Text('Status',
                        style: TextStyle(color: Colors.grey)),
                    StatusChip.fromStatus(sale.paymentStatus ?? 'N/A'),
                  ],
                ),
              ],
            ),
          ),
          const SizedBox(height: 16),
          const SectionTitle('Items', icon: Icons.list_alt),
          ...sale.items.map((it) => AppCard(
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Text(it.medicineName ?? 'Medicine',
                        style: const TextStyle(
                            fontWeight: FontWeight.w600, fontSize: 15)),
                    const SizedBox(height: 4),
                    _row('Qty', '${it.quantity ?? 0}'),
                    _row('Unit Price', _money(it.unitPrice)),
                    _row('Subtotal', _money(it.subtotal)),
                  ],
                ),
              )),
          const SizedBox(height: 16),
          AppCard(
            child: Column(
              children: [
                _row('Total', _money(sale.totalAmount)),
                _row('Discount', _money(sale.discount)),
                _row('VAT', _money(sale.vat)),
                _row('Net Payable', _money(sale.netPayable)),
                _row('Paid', _money(sale.paidAmount)),
              ],
            ),
          ),
        ],
      ),
    );
  }

  Widget _row(String label, String value) => Padding(
        padding: const EdgeInsets.symmetric(vertical: 4),
        child: Row(
          mainAxisAlignment: MainAxisAlignment.spaceBetween,
          children: [
            Text(label, style: const TextStyle(color: Colors.grey)),
            Text(value, style: const TextStyle(fontWeight: FontWeight.w500)),
          ],
        ),
      );
}
