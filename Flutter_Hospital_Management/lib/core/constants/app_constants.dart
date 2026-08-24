class AppConstants {
  // Android emulator maps 10.0.2.2 -> host machine localhost.
  // For Flutter Web / Windows desktop use: 'http://localhost:8085/api'
  // For a physical device use your machine's LAN IP, e.g. 'http://192.168.x.x:8085/api'
  static const String baseUrl = 'http://10.0.2.2:8085/api';

  static const String appName = 'Elite Care Hospital';
  static const String tokenKey = 'auth_token';
  static const String userKey = 'auth_user';
}
