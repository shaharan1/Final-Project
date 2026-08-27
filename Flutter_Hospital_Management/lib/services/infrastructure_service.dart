import 'package:dio/dio.dart';
import 'package:flutter_hospital_management/models/bed.dart';

class InfrastructureService {
  final Dio dio;

  InfrastructureService(this.dio);

  Future<List<Bed>> getBeds() async {
    final res = await dio.get('/infrastructure/beds');
    return (res.data as List)
        .map((e) => Bed.fromJson(e as Map<String, dynamic>))
        .toList();
  }
}
