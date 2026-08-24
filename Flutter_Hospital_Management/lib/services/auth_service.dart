import 'package:dio/dio.dart';
import 'package:flutter_hospital_management/models/login_model.dart';

class AuthService {
  final Dio dio;

  AuthService(this.dio);

  Future<LoginResponse> login(String email, String password) async {
    final response = await dio.post(
      '/auth/login',
      data: {'email': email, 'password': password},
    );
    return LoginResponse.fromJson(response.data as Map<String, dynamic>);
  }
}
