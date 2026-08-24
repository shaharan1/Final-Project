import 'package:dio/dio.dart';
import 'package:flutter_hospital_management/core/constants/app_constants.dart';
import 'package:flutter_hospital_management/core/storage/token_storage.dart';

class DioClient {
  static Dio create({TokenStorage? tokenStorage}) {
    final storage = tokenStorage ?? TokenStorage();
    final dio = Dio(BaseOptions(
      baseUrl: AppConstants.baseUrl,
      connectTimeout: const Duration(seconds: 30),
      receiveTimeout: const Duration(seconds: 30),
      headers: {'Content-Type': 'application/json'},
    ));

    dio.interceptors.add(InterceptorsWrapper(
      onRequest: (options, handler) async {
        final token = await storage.getToken();
        if (token != null && token.isNotEmpty) {
          options.headers['Authorization'] = 'Bearer $token';
        }
        handler.next(options);
      },
      onError: (error, handler) {
        // Centralized error handling can be added here (e.g. 401 -> logout).
        handler.next(error);
      },
    ));

    return dio;
  }
}
