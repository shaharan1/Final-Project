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
      final user = _decode(userJson);
      state = state.copyWith(token: token, user: user);
    }
  }

  Future<void> logout() async {
    await tokenStorage.clear();
    state = const AuthState();
  }

  String _encode(LoginResponse user) {
    // Simple, dependency-free encoding of the stored user json.
    return Uri.encodeComponent(_userToJsonString(user));
  }

  LoginResponse _decode(String encoded) {
    final jsonString = Uri.decodeComponent(encoded);
    final Map<String, dynamic> map = _parseJsonMap(jsonString);
    return LoginResponse.fromJson(map);
  }

  String _userToJsonString(LoginResponse user) {
    final buffer = StringBuffer();
    buffer.write('{');
    buffer.write('"token":"${_escape(user.token)}",');
    buffer.write('"tokenType":"${_escape(user.tokenType)}",');
    buffer.write('"role":"${_escape(user.role)}",');
    buffer.write('"email":"${_escape(user.email)}",');
    buffer.write('"name":"${_escape(user.name)}",');
    buffer.write('"userId":${user.userId},');
    buffer.write('"phone":${user.phone == null ? 'null' : '"${_escape(user.phone!)}"'},');
    buffer.write('"image":${user.image == null ? 'null' : '"${_escape(user.image!)}"'});
    buffer.write('}');
    return buffer.toString();
  }

  Map<String, dynamic> _parseJsonMap(String s) {
    // Minimal parser for our own known shape; relies on valid generated json.
    final result = <String, dynamic>{};
    final trimmed = s.trim();
    if (!trimmed.startsWith('{') || !trimmed.endsWith('}')) return result;
    final inner = trimmed.substring(1, trimmed.length - 1);
    for (final pair in inner.split(',')) {
      final idx = pair.indexOf(':');
      if (idx == -1) continue;
      final key = pair.substring(0, idx).trim().replaceAll('"', '');
      final raw = pair.substring(idx + 1).trim();
      result[key] = _parseValue(raw);
    }
    return result;
  }

  dynamic _parseValue(String raw) {
    if (raw == 'null') return null;
    if (raw.startsWith('"') && raw.endsWith('"')) {
      return raw.substring(1, raw.length - 1).replaceAll('\\"', '"');
    }
    return int.tryParse(raw) ?? raw;
  }

  String _escape(String v) => v.replaceAll('"', '\\"');

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
