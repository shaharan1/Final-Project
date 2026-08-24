import 'dart:convert';
import 'package:dio/dio.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:flutter_hospital_management/core/network/dio_client.dart';
import 'package:flutter_hospital_management/core/storage/token_storage.dart';
import 'package:flutter_hospital_management/models/login_model.dart';
import 'package:flutter_hospital_management/services/auth_service.dart';

final tokenStorageProvider = Provider<TokenStorage>((ref) => TokenStorage());

final dioProvider = Provider<Dio>((ref) {
  final storage = ref.watch(tokenStorageProvider);
  return DioClient.create(tokenStorage: storage);
});

final authServiceProvider = Provider<AuthService>((ref) {
  return AuthService(ref.watch(dioProvider));
});

class AuthState {
  final bool isLoading;
  final String? token;
  final LoginResponse? user;
  final String? error;

  const AuthState({
    this.isLoading = false,
    this.token,
    this.user,
    this.error,
  });

  AuthState copyWith({
    bool? isLoading,
    String? token,
    LoginResponse? user,
    String? error,
  }) {
    return AuthState(
      isLoading: isLoading ?? this.isLoading,
      token: token ?? this.token,
      user: user ?? this.user,
      error: error ?? this.error,
    );
  }
}

final authNotifierProvider =
    StateNotifierProvider<AuthNotifier, AuthState>((ref) {
  return AuthNotifier(
    authService: ref.watch(authServiceProvider),
    tokenStorage: ref.watch(tokenStorageProvider),
  );
});

class AuthNotifier extends StateNotifier<AuthState> {
  final AuthService authService;
  final TokenStorage tokenStorage;

  AuthNotifier({required this.authService, required this.tokenStorage})
      : super(const AuthState());

  Future<void> login(String email, String password) async {
    state = state.copyWith(isLoading: true, error: null);
    try {
      final user = await authService.login(email, password);
      await tokenStorage.saveToken(user.token);
      await tokenStorage.saveUser(_encode(user));
      state = state.copyWith(
        isLoading: false,
        token: user.token,
        user: user,
      );
    } on DioException catch (e) {
      state = state.copyWith(
        isLoading: false,
        error: _extractError(e),
      );
    } catch (e) {
      state = state.copyWith(isLoading: false, error: e.toString());
    }
  }

  Future<void> tryAutoLogin() async {
    final token = await tokenStorage.getToken();
    final userJson = await tokenStorage.getUser();
    if (token != null && token.isNotEmpty && userJson != null) {
      final user =
          LoginResponse.fromJson(jsonDecode(userJson) as Map<String, dynamic>);
      state = state.copyWith(token: token, user: user);
    }
  }

  Future<void> logout() async {
    await tokenStorage.clear();
    state = const AuthState();
  }

  String _encode(LoginResponse user) => jsonEncode(user.toJson());

  String _extractError(DioException e) {
    final data = e.response?.data;
    if (data is Map && data['message'] != null) {
      return data['message'].toString();
    }
    if (e.response?.statusCode == 401) return 'Invalid email or password.';
    if (e.type == DioExceptionType.connectionError) {
      return 'Cannot reach the server. Is the backend running on :8085?';
    }
    return e.message ?? 'Login failed.';
  }
}
