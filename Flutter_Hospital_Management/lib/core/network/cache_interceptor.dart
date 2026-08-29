import 'dart:convert';

import 'package:dio/dio.dart';
import 'package:hive_flutter/hive_flutter.dart';

class CacheInterceptor extends Interceptor {
  final Box box;
  final Duration ttl;

  CacheInterceptor(this.box, [this.ttl = const Duration(hours: 24)]);

  String _key(String uri) => 'cache:$uri';

  @override
  void onResponse(Response response, ResponseInterceptorHandler handler) {
    final req = response.requestOptions;
    if (req.method.toUpperCase() == 'GET') {
      try {
        box.put(_key(req.uri.toString()), {
          't': DateTime.now().millisecondsSinceEpoch,
          'd': jsonEncode(response.data),
        });
      } catch (_) {}
    }
    super.onResponse(response, handler);
  }

  @override
  void onError(DioException err, ErrorInterceptorHandler handler) {
    final req = err.requestOptions;
    final offline = err.type == DioExceptionType.connectionError ||
        err.type == DioExceptionType.connectionTimeout ||
        err.type == DioExceptionType.receiveTimeout ||
        err.type == DioExceptionType.sendTimeout ||
        err.type == DioExceptionType.unknown;

    if (req.method.toUpperCase() == 'GET' && offline) {
      final cached = box.get(_key(req.uri.toString()));
      if (cached is Map) {
        final ts = (cached['t'] as int?) ?? 0;
        if (DateTime.now().millisecondsSinceEpoch - ts < ttl.inMilliseconds) {
          try {
            final data = jsonDecode(cached['d'] as String);
            return handler.resolve(Response(
              data: data,
              requestOptions: req,
              statusCode: 200,
              statusMessage: 'served_from_cache',
            ));
          } catch (_) {}
        }
      }
    }
    super.onError(err, handler);
  }
}
