import 'package:dio/dio.dart';
import 'package:flutter_hospital_management/models/insurance.dart';

class InsuranceService {
  final Dio dio;

  InsuranceService(this.dio);

  Future<List<Insurance>> getInsurances() async {
    final res = await dio.get('/insurance');
    return (res.data as List)
        .map((e) => Insurance.fromJson(e as Map<String, dynamic>))
        .toList();
  }

  Future<List<InsuranceClaim>> getClaims() async {
    final res = await dio.get('/insurance-claims');
    return (res.data as List)
        .map((e) => InsuranceClaim.fromJson(e as Map<String, dynamic>))
        .toList();
  }
}
