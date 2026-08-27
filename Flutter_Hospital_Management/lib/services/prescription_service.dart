import 'package:dio/dio.dart';
import 'package:flutter_hospital_management/models/prescription.dart';

class PrescriptionService {
  final Dio dio;

  PrescriptionService(this.dio);

  Future<PrescriptionResponse> create(PrescriptionRequest request) async {
    final res = await dio.post('/prescriptions', data: request.toJson());
    return PrescriptionResponse.fromJson(res.data as Map<String, dynamic>);
  }

  Future<List<PrescriptionResponse>> getByDoctor(int doctorId) async {
    final res = await dio.get('/prescriptions/doctor/$doctorId');
    return (res.data as List)
        .map((e) => PrescriptionResponse.fromJson(e as Map<String, dynamic>))
        .toList();
  }

  Future<List<PrescriptionResponse>> getAll() async {
    final res = await dio.get('/prescriptions');
    return (res.data as List)
        .map((e) => PrescriptionResponse.fromJson(e as Map<String, dynamic>))
        .toList();
  }

  Future<PrescriptionResponse> getById(int id) async {
    final res = await dio.get('/prescriptions/$id');
    return PrescriptionResponse.fromJson(res.data as Map<String, dynamic>);
  }
}
