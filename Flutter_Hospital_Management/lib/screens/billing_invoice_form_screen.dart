import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:flutter_hospital_management/models/billing_invoice.dart';
import 'package:flutter_hospital_management/models/patient.dart';
import 'package:flutter_hospital_management/providers/auth_provider.dart';
import 'package:flutter_hospital_management/providers/billing_provider.dart';
import 'package:flutter_hospital_management/providers/patient_provider.dart';

class _ItemRow {
  ChargeCategory? category;
  final desc = TextEditingController();
  final qty = TextEditingController(text: '1');
  final price = TextEditingController();

  void dispose() {
    desc.dispose();
    qty.dispose();
    price.dispose();
  }
}

class BillingInvoiceFormScreen extends ConsumerStatefulWidget {
  const BillingInvoiceFormScreen({super.key});

  @override
  ConsumerState<BillingInvoiceFormScreen> createState() =>
      _BillingInvoiceFormScreenState();
}

class _BillingInvoiceFormScreenState
    extends ConsumerState<BillingInvoiceFormScreen> {
  final _formKey = GlobalKey<FormState>();
  Patient? _patient;
  String? _invoiceType;
  final _tax = TextEditingController();
  final _discount = TextEditingController();
  final _notes = TextEditingController();
  final List<_ItemRow> _rows = [];
  bool _saving = false;

  final _types = ['OUTPATIENT', 'INPATIENT', 'EMERGENCY'];

  @override
  void initState() {
    super.initState();
    _invoiceType = 'OUTPATIENT';
    _rows.add(_ItemRow());
    Future.microtask(() {
      ref.read(patientNotifierProvider.notifier).load();
      ref.read(categoryProvider.notifier).load();
    });
  }

  @override
  void dispose() {
    for (final r in _rows) {
      r.dispose();
    }
    _tax.dispose();
    _discount.dispose();
    _notes.dispose();
    super.dispose();
  }

  void _addRow() => setState(() => _rows.add(_ItemRow()));

  void _removeRow(int i) {
    if (_rows.length <= 1) {
      return;
    }
    setState(() {
      _rows[i].dispose();
      _rows.removeAt(i);
    });
  }

  Future<void> _submit() async {
    if (!_formKey.currentState!.validate()) return;
    if (_patient == null) {
      _snack('Please select a patient.');
      return;
    }
    final items = _rows.map((r) {
      return BillingInvoiceItemRequest(
        chargeCategoryId: r.category?.id,
        categoryCode: r.category?.code,
        description: r.desc.text.trim(),
        quantity: int.tryParse(r.qty.text.trim()) ?? 1,
        unitPrice: double.tryParse(r.price.text.trim()) ?? 0,
      );
    }).toList();

    final user = ref.read(authNotifierProvider).user;
    final request = BillingInvoiceRequest(
      patientId: _patient!.id,
      invoiceType: _invoiceType,
      taxRate: double.tryParse(_tax.text.trim()),
      discountPercent: double.tryParse(_discount.text.trim()),
      notes: _notes.text.trim(),
      preparedBy: user?.name ?? 'System',
      items: items,
    );

    setState(() => _saving = true);
    final ok = await ref.read(billingNotifierProvider.notifier).create(request);
    setState(() => _saving = false);
    if (ok && mounted) Navigator.pop(context);
  }

  void _snack(String m) => ScaffoldMessenger.of(context)
      .showSnackBar(SnackBar(content: Text(m), backgroundColor: Colors.red));

  @override
  Widget build(BuildContext context) {
    final patients = ref.watch(patientNotifierProvider).patients;
    final categories = ref.watch(categoryProvider).categories;

    return Scaffold(
      appBar: AppBar(
        title: const Text('New Invoice'),
        backgroundColor: Colors.teal,
        foregroundColor: Colors.white,
      ),
      body: SingleChildScrollView(
        padding: const EdgeInsets.all(16),
        child: Form(
          key: _formKey,
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
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
              DropdownButtonFormField<String>(
                initialValue: _invoiceType,
                decoration: const InputDecoration(labelText: 'Invoice Type', border: OutlineInputBorder()),
                items: _types
                    .map((t) => DropdownMenuItem(value: t, child: Text(t)))
                    .toList(),
                onChanged: (v) => setState(() => _invoiceType = v),
              ),
              const SizedBox(height: 12),
              Row(
                children: [
                  Expanded(
                    child: TextFormField(
                      controller: _tax,
                      decoration: const InputDecoration(labelText: 'Tax Rate %', border: OutlineInputBorder()),
                      keyboardType: const TextInputType.numberWithOptions(decimal: true),
                    ),
                  ),
                  const SizedBox(width: 12),
                  Expanded(
                    child: TextFormField(
                      controller: _discount,
                      decoration: const InputDecoration(labelText: 'Discount %', border: OutlineInputBorder()),
                      keyboardType: const TextInputType.numberWithOptions(decimal: true),
                    ),
                  ),
                ],
              ),
              const SizedBox(height: 12),
              TextFormField(
                controller: _notes,
                decoration: const InputDecoration(labelText: 'Notes', border: OutlineInputBorder()),
              ),
              const SizedBox(height: 16),
              const Text('Items', style: TextStyle(fontWeight: FontWeight.bold, fontSize: 16)),
              const SizedBox(height: 8),
              ..._rows.asMap().entries.map((entry) {
                final i = entry.key;
                final r = entry.value;
                return Card(
                  margin: const EdgeInsets.only(bottom: 12),
                  child: Padding(
                    padding: const EdgeInsets.all(12),
                    child: Column(
                      children: [
                        Row(
                          children: [
                            Expanded(
                              child: DropdownButtonFormField<ChargeCategory>(
                                initialValue: r.category,
                                decoration: const InputDecoration(labelText: 'Category', border: OutlineInputBorder()),
                                items: categories
                                    .map((c) => DropdownMenuItem(value: c, child: Text(c.display)))
                                    .toList(),
                                onChanged: (v) {
                                  setState(() {
                                    r.category = v;
                                    if (v?.defaultUnitPrice != null) {
                                      r.price.text = v!.defaultUnitPrice.toString();
                                    }
                                  });
                                },
                              ),
                            ),
                            IconButton(
                              icon: const Icon(Icons.delete, color: Colors.red),
                              onPressed: () => _removeRow(i),
                            ),
                          ],
                        ),
                        const SizedBox(height: 8),
                        TextFormField(
                          controller: r.desc,
                          decoration: const InputDecoration(labelText: 'Description', border: OutlineInputBorder()),
                        ),
                        const SizedBox(height: 8),
                        Row(
                          children: [
                            Expanded(
                              child: TextFormField(
                                controller: r.qty,
                                decoration: const InputDecoration(labelText: 'Qty', border: OutlineInputBorder()),
                                keyboardType: TextInputType.number,
                              ),
                            ),
                            const SizedBox(width: 12),
                            Expanded(
                              child: TextFormField(
                                controller: r.price,
                                decoration: const InputDecoration(labelText: 'Unit Price', border: OutlineInputBorder()),
                                keyboardType: const TextInputType.numberWithOptions(decimal: true),
                              ),
                            ),
                          ],
                        ),
                      ],
                    ),
                  ),
                );
              }),
              TextButton.icon(
                onPressed: _addRow,
                icon: const Icon(Icons.add),
                label: const Text('Add Item'),
              ),
              const SizedBox(height: 16),
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
                      : const Text('Create Invoice'),
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
