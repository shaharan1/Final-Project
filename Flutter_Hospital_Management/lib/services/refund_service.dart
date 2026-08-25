import 'package:dio/dio.dart';
import 'package:flutter_hospital_management/models/billing_invoice.dart';

class RefundService {
  final Dio dio;

  RefundService(this.dio);

  Future<void> create(RefundRequestModel request) async {
    await dio.post('/refunds', data: request.toJson());
  }
}
