import 'package:dio/dio.dart';
import 'package:flutter_hospital_management/models/admission.dart';

class AdmissionService {
  final Dio dio;

  AdmissionService(this.dio);

  Future<List<Admission>> getAll() async {
    final res = await dio.get('/admissions');
    return (res.data as List)
        .map((e) => Admission.fromJson(e as Map<String, dynamic>))
        .toList();
  }

  Future<List<Admission>> getActive() async {
    final res = await dio.get('/admissions/active');
    return (res.data as List)
        .map((e) => Admission.fromJson(e as Map<String, dynamic>))
        .toList();
  }

  Future<Admission> admit(AdmissionRequest request) async {
    final res = await dio.post('/admissions/admit', data: request.toJson());
    return Admission.fromJson(res.data as Map<String, dynamic>);
  }

  Future<Admission> discharge(int id) async {
    final res = await dio.put('/admissions/discharge/$id');
    return Admission.fromJson(res.data as Map<String, dynamic>);
  }
}
