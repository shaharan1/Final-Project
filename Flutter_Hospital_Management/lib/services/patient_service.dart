import 'package:dio/dio.dart';
import 'package:flutter_hospital_management/models/patient.dart';

class PatientService {
  final Dio dio;

  PatientService(this.dio);

  Future<List<Patient>> getAll() async {
    final res = await dio.get('/patients');
    return (res.data as List)
        .map((e) => Patient.fromJson(e as Map<String, dynamic>))
        .toList();
  }

  Future<List<Patient>> search(String keyword) async {
    final res = await dio.get(
      '/patients/search',
      queryParameters: {'keyword': keyword},
    );
    return (res.data as List)
        .map((e) => Patient.fromJson(e as Map<String, dynamic>))
        .toList();
  }

  Future<Patient> getById(int id) async {
    final res = await dio.get('/patients/$id');
    return Patient.fromJson(res.data as Map<String, dynamic>);
  }

  Future<Patient> create(PatientRequest request) async {
    final res = await dio.post('/patients', data: request.toJson());
    return Patient.fromJson(res.data as Map<String, dynamic>);
  }

  Future<Patient> update(int id, PatientRequest request) async {
    final res = await dio.put('/patients/$id', data: request.toJson());
    return Patient.fromJson(res.data as Map<String, dynamic>);
  }

  Future<void> delete(int id) async {
    await dio.delete('/patients/$id');
  }
}
