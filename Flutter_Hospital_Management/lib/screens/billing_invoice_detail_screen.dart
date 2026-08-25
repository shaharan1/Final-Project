import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:flutter_hospital_management/models/billing_invoice.dart';
import 'package:flutter_hospital_management/providers/auth_provider.dart';
import 'package:flutter_hospital_management/providers/billing_provider.dart';

class BillingInvoiceDetailScreen extends ConsumerStatefulWidget {
  final int invoiceId;
  const BillingInvoiceDetailScreen({super.key, required this.invoiceId});

  @override
  ConsumerState<BillingInvoiceDetailScreen> createState() =>
      _BillingInvoiceDetailScreenState();
}

class _BillingInvoiceDetailScreenState
    extends ConsumerState<BillingInvoiceDetailScreen> {
  @override
  void initState() {
    super.initState();
    Future.microtask(() {
      ref.read(billingDetailProvider(widget.invoiceId).notifier).load();
      ref.read(categoryProvider.notifier).load();
    });
  }

  String _money(double? v) => (v ?? 0).toStringAsFixed(2);

  @override
  Widget build(BuildContext context) {
    final state = ref.watch(billingDetailProvider(widget.invoiceId));
    final inv = state.invoice;

    return Scaffold(
      appBar: AppBar(
        title: Text(inv?.invoiceNumber ?? 'Invoice'),
        backgroundColor: Colors.teal,
        foregroundColor: Colors.white,
      ),
      body: state.isLoading
          ? const Center(child: CircularProgressIndicator())
          : state.error != null
              ? Center(child: Text(state.error!, style: const TextStyle(color: Colors.red)))
              : inv == null
                  ? const Center(child: Text('Invoice not found.'))
                  : SingleChildScrollView(
                      padding: const EdgeInsets.all(16),
                      child: Column(
                        crossAxisAlignment: CrossAxisAlignment.start,
                        children: [
                          _Header(inv: inv),
                          const SizedBox(height: 16),
                          const Text('Items', style: TextStyle(fontWeight: FontWeight.bold, fontSize: 16)),
                          const SizedBox(height: 8),
                          ...inv.items.map((it) => Card(
                                child: ListTile(
                                  title: Text(it.categoryName ?? it.description ?? 'Item'),
                                  subtitle: Text(
                                      'Qty: ${it.quantity ?? 0}  x  ${_money(it.unitPrice)}  (disc ${it.discountPercent ?? 0}%)'),
                                  trailing: Text(_money(it.amount)),
                                ),
                              )),
                          const SizedBox(height: 16),
                          const Text('Payments', style: TextStyle(fontWeight: FontWeight.bold, fontSize: 16)),
                          const SizedBox(height: 8),
                          ...state.payments.map((p) => ListTile(
                                leading: const Icon(Icons.payment),
                                title: Text('${p.paymentMethod}  •  ${p.paymentStatus}'),
                                subtitle: Text('by ${p.processedBy ?? '?'}  ${p.transactionId ?? ''}'),
                                trailing: Text(_money(p.amount)),
                              )),
                          if (state.payments.isEmpty)
                            const Text('No payments yet.', style: TextStyle(color: Colors.grey)),
                          const SizedBox(height: 24),
                          Wrap(
                            spacing: 12,
                            runSpacing: 12,
                            children: [
                              if (inv.invoiceStatus != 'FINALIZED')
                                ElevatedButton.icon(
                                  icon: const Icon(Icons.check),
                                  label: const Text('Finalize'),
                                  style: _btn(),
                                  onPressed: () => _finalize(inv),
                                ),
                              if (inv.invoiceStatus != 'FINALIZED')
                                ElevatedButton.icon(
                                  icon: const Icon(Icons.add_shopping_cart),
                                  label: const Text('Add Item'),
                                  style: _btn(),
                                  onPressed: () => _addItemDialog(),
                                ),
                              ElevatedButton.icon(
                                icon: const Icon(Icons.payment),
                                label: const Text('Record Payment'),
                                style: _btn(),
                                onPressed: () => _paymentDialog(inv),
                              ),
                              ElevatedButton.icon(
                                icon: const Icon(Icons.undo),
                                label: const Text('Refund'),
                                style: _btn(Colors.deepOrange),
                                onPressed: () => _refundDialog(inv),
                              ),
                            ],
                          ),
                        ],
                      ),
                    ),
    );
  }

  ButtonStyle _btn([Color? c]) => ElevatedButton.styleFrom(
        backgroundColor: c ?? Colors.teal,
        foregroundColor: Colors.white,
        padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 12),
      );

  Future<void> _finalize(BillingInvoice inv) async {
    final user = ref.read(authNotifierProvider).user;
    final ok = await ref
        .read(billingDetailProvider(widget.invoiceId).notifier)
        .finalize(user?.name ?? 'System');
    if (!ok && mounted) {
      _snack(ref.read(billingDetailProvider(widget.invoiceId)).error ??
          'Finalize failed');
    }
  }

  Future<void> _addItemDialog() async {
    final categories = ref.read(categoryProvider).categories;
    ChargeCategory? cat;
    final desc = TextEditingController();
    final qty = TextEditingController(text: '1');
    final price = TextEditingController();

    await showDialog(
      context: context,
      builder: (ctx) => AlertDialog(
        title: const Text('Add Item'),
        content: SingleChildScrollView(
          child: Column(
            mainAxisSize: MainAxisSize.min,
            children: [
              DropdownButtonFormField<ChargeCategory>(
                initialValue: cat,
                decoration: const InputDecoration(labelText: 'Category', border: OutlineInputBorder()),
                items: categories
                    .map((c) => DropdownMenuItem(value: c, child: Text(c.display)))
                    .toList(),
                onChanged: (v) {
                  cat = v;
                  if (v?.defaultUnitPrice != null) {
                    price.text = v!.defaultUnitPrice.toString();
                  }
                },
              ),
              const SizedBox(height: 12),
              TextField(
                controller: desc,
                decoration: const InputDecoration(labelText: 'Description', border: OutlineInputBorder()),
              ),
              const SizedBox(height: 12),
              Row(
                children: [
                  Expanded(
                    child: TextField(
                      controller: qty,
                      decoration: const InputDecoration(labelText: 'Qty', border: OutlineInputBorder()),
                      keyboardType: TextInputType.number,
                    ),
                  ),
                  const SizedBox(width: 12),
                  Expanded(
                    child: TextField(
                      controller: price,
                      decoration: const InputDecoration(labelText: 'Unit Price', border: OutlineInputBorder()),
                      keyboardType: const TextInputType.numberWithOptions(decimal: true),
                    ),
                  ),
                ],
              ),
            ],
          ),
        ),
        actions: [
          TextButton(onPressed: () => Navigator.pop(ctx), child: const Text('Cancel')),
          TextButton(
            onPressed: () async {
              final item = BillingInvoiceItemRequest(
                chargeCategoryId: cat?.id,
                categoryCode: cat?.code,
                description: desc.text.trim(),
                quantity: int.tryParse(qty.text.trim()) ?? 1,
                unitPrice: double.tryParse(price.text.trim()) ?? 0,
              );
              Navigator.pop(ctx);
              final ok = await ref
                  .read(billingDetailProvider(widget.invoiceId).notifier)
                  .addItem(item);
              if (!ok && mounted) {
                _snack(ref.read(billingDetailProvider(widget.invoiceId)).error ??
                    'Add item failed');
              }
            },
            child: const Text('Add'),
          ),
        ],
      ),
    );
  }

  Future<void> _paymentDialog(BillingInvoice inv) async {
    final amount = TextEditingController(text: _money(inv.dueAmount));
    String method = 'CASH';
    final txn = TextEditingController();
    final methods = ['CASH', 'CARD', 'BANK_TRANSFER', 'MOBILE_BANKING', 'INSURANCE', 'SPLIT'];

    await showDialog(
      context: context,
      builder: (ctx) => AlertDialog(
        title: const Text('Record Payment'),
        content: SingleChildScrollView(
          child: Column(
            mainAxisSize: MainAxisSize.min,
            children: [
              TextField(
                controller: amount,
                decoration: const InputDecoration(labelText: 'Amount', border: OutlineInputBorder()),
                keyboardType: const TextInputType.numberWithOptions(decimal: true),
              ),
              const SizedBox(height: 12),
              DropdownButtonFormField<String>(
                initialValue: method,
                decoration: const InputDecoration(labelText: 'Method', border: OutlineInputBorder()),
                items: methods.map((m) => DropdownMenuItem(value: m, child: Text(m))).toList(),
                onChanged: (v) => method = v!,
              ),
              const SizedBox(height: 12),
              TextField(
                controller: txn,
                decoration: const InputDecoration(labelText: 'Transaction ID (optional)', border: OutlineInputBorder()),
              ),
            ],
          ),
        ),
        actions: [
          TextButton(onPressed: () => Navigator.pop(ctx), child: const Text('Cancel')),
          TextButton(
            onPressed: () async {
              final user = ref.read(authNotifierProvider).user;
              final req = BillingPaymentRequest(
                invoiceId: inv.id,
                amount: double.tryParse(amount.text.trim()),
                paymentMethod: method,
                transactionId: txn.text.trim(),
                processedBy: user?.name ?? 'System',
              );
              Navigator.pop(ctx);
              final ok = await ref
                  .read(billingDetailProvider(widget.invoiceId).notifier)
                  .recordPayment(req);
              if (!ok && mounted) {
                _snack(ref.read(billingDetailProvider(widget.invoiceId)).error ??
                    'Payment failed');
              }
            },
            child: const Text('Pay'),
          ),
        ],
      ),
    );
  }

  Future<void> _refundDialog(BillingInvoice inv) async {
    final amount = TextEditingController();
    final reason = TextEditingController();
    String type = 'PARTIAL';
    final types = ['FULL', 'PARTIAL', 'ADJUSTMENT'];

    await showDialog(
      context: context,
      builder: (ctx) => AlertDialog(
        title: const Text('Refund'),
        content: SingleChildScrollView(
          child: Column(
            mainAxisSize: MainAxisSize.min,
            children: [
              TextField(
                controller: amount,
                decoration: const InputDecoration(labelText: 'Refund Amount', border: OutlineInputBorder()),
                keyboardType: const TextInputType.numberWithOptions(decimal: true),
              ),
              const SizedBox(height: 12),
              DropdownButtonFormField<String>(
                initialValue: type,
                decoration: const InputDecoration(labelText: 'Type', border: OutlineInputBorder()),
                items: types.map((t) => DropdownMenuItem(value: t, child: Text(t))).toList(),
                onChanged: (v) => type = v!,
              ),
              const SizedBox(height: 12),
              TextField(
                controller: reason,
                decoration: const InputDecoration(labelText: 'Reason', border: OutlineInputBorder()),
              ),
            ],
          ),
        ),
        actions: [
          TextButton(onPressed: () => Navigator.pop(ctx), child: const Text('Cancel')),
          TextButton(
            onPressed: () async {
              final user = ref.read(authNotifierProvider).user;
              final req = RefundRequestModel(
                invoiceNumber: inv.invoiceNumber,
                patientId: inv.patientId,
                patientName: inv.patientName,
                refundAmount: double.tryParse(amount.text.trim()),
                refundReason: reason.text.trim(),
                refundType: type,
                processedBy: user?.name ?? 'System',
              );
              Navigator.pop(ctx);
              try {
                await ref.read(refundServiceProvider).create(req);
                await ref
                    .read(billingDetailProvider(widget.invoiceId).notifier)
                    .load();
              } catch (e) {
                if (mounted) _snack(e.toString());
              }
            },
            child: const Text('Submit'),
          ),
        ],
      ),
    );
  }

  void _snack(String m) => ScaffoldMessenger.of(context)
      .showSnackBar(SnackBar(content: Text(m), backgroundColor: Colors.red));
}

class _Header extends StatelessWidget {
  final BillingInvoice inv;
  const _Header({required this.inv});

  String _money(double? v) => (v ?? 0).toStringAsFixed(2);

  @override
  Widget build(BuildContext context) {
    return Card(
      child: Padding(
        padding: const EdgeInsets.all(16),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Text(inv.patientName ?? 'Unknown',
                style: Theme.of(context).textTheme.titleLarge),
            Text('${inv.patientCode ?? ''}  •  ${inv.invoiceType ?? ''}'),
            const Divider(),
            _row('Subtotal', _money(inv.subtotal)),
            _row('Tax (${inv.taxRate ?? 0}%)', _money(inv.taxAmount)),
            _row('Discount (${inv.discountPercent ?? 0}%)', _money(inv.discountAmount)),
            _row('Net Amount', _money(inv.netAmount)),
            _row('Total Paid', _money(inv.totalPaid)),
            _row('Due', _money(inv.dueAmount)),
            const Divider(),
            _row('Payment Status', inv.paymentStatus ?? ''),
            _row('Invoice Status', inv.invoiceStatus ?? ''),
            _row('Prepared By', inv.preparedBy ?? ''),
          ],
        ),
      ),
    );
  }

  Widget _row(String k, String v) => Padding(
        padding: const EdgeInsets.symmetric(vertical: 2),
        child: Row(
          mainAxisAlignment: MainAxisAlignment.spaceBetween,
          children: [
            Text(k, style: const TextStyle(color: Colors.grey)),
            Text(v, style: const TextStyle(fontWeight: FontWeight.w600)),
          ],
        ),
      );
}
