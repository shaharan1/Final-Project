import 'package:flutter/foundation.dart' show kIsWeb;

class AppConstants {
  // Web runs in a browser, so the backend is just localhost.
  // The Android emulator maps 10.0.2.2 -> host machine localhost.
  // For a physical device use your machine's LAN IP, e.g. 'http://192.168.x.x:8085/api'
  static String get baseUrl =>
      kIsWeb ? 'http://localhost:8085/api' : 'http://10.0.2.2:8085/api';

  static const String appName = 'Elite Care Hospital';
  static const String tokenKey = 'auth_token';
  static const String userKey = 'auth_user';
}
