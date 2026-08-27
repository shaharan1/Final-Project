import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:flutter_hospital_management/models/medicine.dart';
import 'package:flutter_hospital_management/providers/pharmacy_provider.dart';

class MedicineFormScreen extends ConsumerStatefulWidget {
  const MedicineFormScreen({super.key});

  @override
  ConsumerState<MedicineFormScreen> createState() => _MedicineFormScreenState();
}

class _MedicineFormScreenState extends ConsumerState<MedicineFormScreen> {
  final _formKey = GlobalKey<FormState>();
  final _name = TextEditingController();
  final _dosage = TextEditingController();
  final _genericId = TextEditingController();
  bool _saving = false;

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('Add Medicine')),
      body: SingleChildScrollView(
        padding: const EdgeInsets.all(16),
        child: Form(
          key: _formKey,
          child: Column(
            children: [
              TextFormField(
                controller: _name,
                decoration: const InputDecoration(labelText: 'Medicine Name *'),
                validator: (v) =>
                    v == null || v.isEmpty ? 'Required' : null,
              ),
              const SizedBox(height: 14),
              TextFormField(
                controller: _dosage,
                decoration: const InputDecoration(labelText: 'Dosage'),
              ),
              const SizedBox(height: 14),
              TextFormField(
                controller: _genericId,
                decoration:
                    const InputDecoration(labelText: 'Generic ID (optional)'),
                keyboardType: TextInputType.number,
              ),
              const SizedBox(height: 24),
              SizedBox(
                width: double.infinity,
                child: ElevatedButton(
                  onPressed: _saving ? null : _submit,
                  child: _saving
                      ? const SizedBox(
                          height: 18,
                          width: 18,
                          child: CircularProgressIndicator(
                              strokeWidth: 2, color: Colors.white))
                      : const Text('Save Medicine'),
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }

  Future<void> _submit() async {
    if (!_formKey.currentState!.validate()) return;
    setState(() => _saving = true);
    final ok = await ref.read(medicineNotifierProvider.notifier).create(
          Medicine(
            medicineName: _name.text.trim(),
            dosage: _dosage.text.trim().isEmpty ? null : _dosage.text.trim(),
            genericId: int.tryParse(_genericId.text.trim()),
          ),
        );
    setState(() => _saving = false);
    if (mounted) {
      if (ok) {
        Navigator.pop(context);
      } else {
        ScaffoldMessenger.of(context).showSnackBar(
            const SnackBar(content: Text('Failed to save medicine')));
      }
    }
  }
}
