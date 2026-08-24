class LoginResponse {
  final String token;
  final String tokenType;
  final String role;
  final String email;
  final String name;
  final int userId;
  final String? phone;
  final String? image;

  LoginResponse({
    required this.token,
    required this.tokenType,
    required this.role,
    required this.email,
    required this.name,
    required this.userId,
    this.phone,
    this.image,
  });

  factory LoginResponse.fromJson(Map<String, dynamic> json) {
    return LoginResponse(
      token: json['token'] as String,
      tokenType: json['tokenType'] as String? ?? 'Bearer',
      role: json['role'] as String? ?? '',
      email: json['email'] as String? ?? '',
      name: json['name'] as String? ?? '',
      userId: json['userId'] as int? ?? 0,
      phone: json['phone'] as String?,
      image: json['image'] as String?,
    );
  }

  Map<String, dynamic> toJson() => {
        'token': token,
        'tokenType': tokenType,
        'role': role,
        'email': email,
        'name': name,
        'userId': userId,
        'phone': phone,
        'image': image,
      };
}
