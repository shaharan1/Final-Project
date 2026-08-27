import 'package:dio/dio.dart';
import 'package:flutter_hospital_management/models/medicine.dart';
import 'package:flutter_hospital_management/models/pharmacy_sale.dart';

class PharmacyService {
  final Dio dio;

  PharmacyService(this.dio);

  Future<List<Medicine>> getMedicines() async {
    final res = await dio.get('/medicines');
    return (res.data as List)
        .map((e) => Medicine.fromJson(e as Map<String, dynamic>))
        .toList();
  }

  Future<List<Medicine>> searchMedicines(String keyword) async {
    final res = await dio.get('/medicines/search',
        queryParameters: {'keyword': keyword});
    return (res.data as List)
        .map((e) => Medicine.fromJson(e as Map<String, dynamic>))
        .toList();
  }

  Future<Medicine> createMedicine(Medicine m) async {
    final res = await dio.post('/medicines', data: m.toJson());
    return Medicine.fromJson(res.data as Map<String, dynamic>);
  }

  Future<List<PharmacySale>> getSales() async {
    final res = await dio.get('/pharmacy/sales');
    return (res.data as List)
        .map((e) => PharmacySale.fromJson(e as Map<String, dynamic>))
        .toList();
  }

  Future<PharmacySale> getSale(int id) async {
    final res = await dio.get('/pharmacy/sales/$id');
    return PharmacySale.fromJson(res.data as Map<String, dynamic>);
  }
}
