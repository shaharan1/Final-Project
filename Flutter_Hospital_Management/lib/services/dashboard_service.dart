import 'package:dio/dio.dart';
import 'package:flutter_hospital_management/models/dashboard.dart';

class DashboardService {
  final Dio dio;

  DashboardService(this.dio);

  Future<BillingDashboard> getBillingSummary() async {
    final res = await dio.get('/billing-invoices/dashboard-summary');
    return BillingDashboard.fromJson(res.data as Map<String, dynamic>);
  }

  Future<Map<String, dynamic>> getPaymentStats() async {
    final res = await dio.get('/payments/dashboard-stats');
    return Map<String, dynamic>.from(res.data as Map);
  }
}
