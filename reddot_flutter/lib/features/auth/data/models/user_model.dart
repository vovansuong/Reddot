import 'dart:convert';
import 'dart:ffi';

import 'package:flutterapp/features/auth/domain/entities/user_entity.dart';

class UserModel extends UserEntity {
  const UserModel(
      {required super.id,
      required super.username,
      required super.email,
      required super.name,
      required super.avatar,
      required super.imageUrl,
      required super.accessToken});

  factory UserModel.fromMap(Map<String, dynamic> json) {
    return UserModel(
      id: (json['id'] ?? 0) as int,
      username: (json['username'] ?? "") as String,
      email: (json['email'] ?? "") as String,
      name: (json['name'] ?? "") as String,
      avatar: (json['avatar'] ?? "") as String,
      imageUrl: (json['imageUrl'] ?? "") as String,
      accessToken: (json['accessToken'] ?? "") as String,
    );
  }

  factory UserModel.fromJson(source) => UserModel.fromMap(json.decode(source));
}