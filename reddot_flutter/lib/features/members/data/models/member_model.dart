import 'dart:convert';
import 'dart:ffi';

import 'package:flutterapp/features/members/domain/entities/member_entity.dart';

class MemberModel extends MemberEntity {
  MemberModel({
    required int userId,
    required String username,
    required String name,
    required String email,
    required String avatar,
    required String imageUrl,
    required int totalDiscussions,
    required int totalComments,
    required int reputation,
    required String status,
  }) : super(
          userId: userId,
          username: username,
          name: name,
          email: email,
          avatar: avatar,
          imageUrl: imageUrl,
          totalDiscussions: totalDiscussions,
          totalComments: totalComments,
          reputation: reputation,
          status: status,
        );

  factory MemberModel.fromMap(Map<String, dynamic> json) {
    return MemberModel(
      userId: (json['userId'] ?? 0) as int,
      username: (json['username'] ?? "") as String,
      name: (json['name'] ?? "") as String,
      email: (json['email'] ?? "") as String,
      avatar: (json['avatar'] ?? "") as String,
      imageUrl: (json['imageUrl'] ?? "") as String,
      totalDiscussions: (json['totalDiscussions'] ?? 0) as int,
      totalComments: (json['totalComments'] ?? 0) as int,
      reputation: (json['reputation'] ?? 0) as int,
      status: (json['status'] ?? "") as String,
    );
  }

  factory MemberModel.fromJson(source) =>
      MemberModel.fromMap(json.decode(source));
}