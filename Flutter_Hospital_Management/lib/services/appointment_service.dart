import 'package:dio/dio.dart';
import 'package:flutter_hospital_management/models/appointment.dart';

class AppointmentService {
  final Dio dio;

  AppointmentService(this.dio);

  Future<List<Appointment>> getAll() async {
    final res = await dio.get('/appointments');
    return (res.data as List)
        .map((e) => Appointment.fromJson(e as Map<String, dynamic>))
        .toList();
  }

  Future<List<Appointment>> getByDoctor(int doctorId) async {
    final res = await dio.get('/appointments/doctor/$doctorId');
    return (res.data as List)
        .map((e) => Appointment.fromJson(e as Map<String, dynamic>))
        .toList();
  }

  Future<List<Appointment>> search(String query) async {
    final res = await dio.get(
      '/appointments/search',
      queryParameters: {'query': query},
    );
    return (res.data as List)
        .map((e) => Appointment.fromJson(e as Map<String, dynamic>))
        .toList();
  }

  Future<Appointment> book(AppointmentRequest request) async {
    final res = await dio.post('/appointments', data: request.toJson());
    return Appointment.fromJson(res.data as Map<String, dynamic>);
  }

  Future<Appointment> cancel(int id) async {
    final res = await dio.put('/appointments/$id/cancel');
    return Appointment.fromJson(res.data as Map<String, dynamic>);
  }
}
