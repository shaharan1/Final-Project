import 'package:dio/dio.dart';
import 'package:flutter_hospital_management/models/ambulance.dart';

class AmbulanceService {
  final Dio dio;

  AmbulanceService(this.dio);

  Future<List<Ambulance>> getAmbulances() async {
    final res = await dio.get('/emergency/ambulances');
    return (res.data as List)
        .map((e) => Ambulance.fromJson(e as Map<String, dynamic>))
        .toList();
  }

  Future<List<AmbulanceTrip>> getTrips() async {
    final res = await dio.get('/emergency/ambulance-trips');
    return (res.data as List)
        .map((e) => AmbulanceTrip.fromJson(e as Map<String, dynamic>))
        .toList();
  }
}
