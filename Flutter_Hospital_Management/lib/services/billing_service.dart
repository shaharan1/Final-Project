import 'package:dio/dio.dart';
import 'package:flutter_hospital_management/models/billing_invoice.dart';

class BillingService {
  final Dio dio;

  BillingService(this.dio);

  Future<List<BillingInvoice>> getAll() async {
    final res = await dio.get('/billing-invoices');
    return (res.data as List)
        .map((e) => BillingInvoice.fromJson(e as Map<String, dynamic>))
        .toList();
  }

  Future<BillingInvoice> getById(int id) async {
    final res = await dio.get('/billing-invoices/$id');
    return BillingInvoice.fromJson(res.data as Map<String, dynamic>);
  }

  Future<List<BillingInvoice>> search(String q) async {
    final res = await dio.get(
      '/billing-invoices/search',
      queryParameters: {'search': q},
    );
    return (res.data as List)
        .map((e) => BillingInvoice.fromJson(e as Map<String, dynamic>))
        .toList();
  }

  Future<BillingInvoice> create(BillingInvoiceRequest request) async {
    final res = await dio.post('/billing-invoices', data: request.toJson());
    return BillingInvoice.fromJson(res.data as Map<String, dynamic>);
  }

  Future<BillingInvoice> addItem(
      int invoiceId, BillingInvoiceItemRequest item) async {
    final res = await dio.post('/billing-invoices/$invoiceId/items',
        data: item.toJson());
    return BillingInvoice.fromJson(res.data as Map<String, dynamic>);
  }

  Future<BillingInvoice> finalize(int id, String finalizedBy) async {
    final res = await dio.put('/billing-invoices/$id/finalize',
        queryParameters: {'finalizedBy': finalizedBy});
    return BillingInvoice.fromJson(res.data as Map<String, dynamic>);
  }

  Future<List<BillingPayment>> getPayments(int invoiceId) async {
    final res = await dio.get('/billing-invoices/$invoiceId/payments');
    return (res.data as List)
        .map((e) => BillingPayment.fromJson(e as Map<String, dynamic>))
        .toList();
  }

  Future<BillingPayment> processPayment(BillingPaymentRequest request) async {
    final res =
        await dio.post('/billing-invoices/payments', data: request.toJson());
    return BillingPayment.fromJson(res.data as Map<String, dynamic>);
  }

  Future<List<ChargeCategory>> getCategories() async {
    final res = await dio.get('/billing-invoices/categories');
    return (res.data as List)
        .map((e) => ChargeCategory.fromJson(e as Map<String, dynamic>))
        .toList();
  }
}
