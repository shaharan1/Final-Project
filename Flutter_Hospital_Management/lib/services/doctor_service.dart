import 'package:dio/dio.dart';
import 'package:flutter_hospital_management/models/doctor.dart';

class DoctorService {
  final Dio dio;

  DoctorService(this.dio);

  Future<List<Doctor>> getAll() async {
    final res = await dio.get('/doctors');
    return (res.data as List)
        .map((e) => Doctor.fromJson(e as Map<String, dynamic>))
        .toList();
  }
}
