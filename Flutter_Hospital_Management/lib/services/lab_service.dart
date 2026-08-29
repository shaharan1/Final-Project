import 'package:dio/dio.dart';
import 'package:flutter_hospital_management/models/test_order.dart';
import 'package:flutter_hospital_management/models/lab_report.dart';
import 'package:flutter_hospital_management/models/test_master.dart';

class LabService {
  final Dio dio;

  LabService(this.dio);

  Future<List<TestOrder>> getTestOrders() async {
    final res = await dio.get('/test-orders');
    return (res.data as List)
        .map((e) => TestOrder.fromJson(e as Map<String, dynamic>))
        .toList();
  }

  Future<List<TestMaster>> getTestMasters() async {
    final res = await dio.get('/tests');
    return (res.data as List)
        .map((e) => TestMaster.fromJson(e as Map<String, dynamic>))
        .toList();
  }

  Future<TestOrder> advanceStatus(int id, String action, Map<String, String> body) async {
    final res = await dio.put('/test-orders/$id/$action', data: body);
    return TestOrder.fromJson(res.data as Map<String, dynamic>);
  }

  Future<List<LabReport>> getReports() async {
    final res = await dio.get('/lab/reports');
    return (res.data as List)
        .map((e) => LabReport.fromJson(e as Map<String, dynamic>))
        .toList();
  }

  Future<LabDashboard> getDashboard() async {
    final res = await dio.get('/lab/reports/dashboard');
    return LabDashboard.fromJson(res.data as Map<String, dynamic>);
  }
}
