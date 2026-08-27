import 'package:dio/dio.dart';
import 'package:flutter_hospital_management/models/clinical.dart';

class DietPlanService {
  final Dio dio;

  DietPlanService(this.dio);

  Future<List<DietPlan>> getDietPlans() async {
    final res = await dio.get('/diet-plans');
    return (res.data as List)
        .map((e) => DietPlan.fromJson(e as Map<String, dynamic>))
        .toList();
  }
}

class SurgeryService {
  final Dio dio;

  SurgeryService(this.dio);

  Future<List<Surgery>> getSurgeries() async {
    final res = await dio.get('/surgeries');
    return (res.data as List)
        .map((e) => Surgery.fromJson(e as Map<String, dynamic>))
        .toList();
  }

  Future<List<SurgeryMaster>> getSurgeryMasters() async {
    final res = await dio.get('/surgery-masters');
    return (res.data as List)
        .map((e) => SurgeryMaster.fromJson(e as Map<String, dynamic>))
        .toList();
  }
}
