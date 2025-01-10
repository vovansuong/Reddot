import 'dart:convert';

import 'package:flutterapp/features/forums/domain/entities/forum_entity.dart';

class ForumModel extends ForumEntity {
  const ForumModel(
      {required super.id,
      required super.title,
      required super.groupId,
      required super.groupName,
      required super.totalComments,
      required super.discussions});

  factory ForumModel.fromMap(Map<dynamic, dynamic> json) {
    List jsonResponse = json["discussions"] ?? [];
    List<DiscussionModel> discussions =
        jsonResponse.map((e) => DiscussionModel.fromMap(e)).toList();

    return ForumModel(
      id: (json['id'] ?? 0) as int,
      title: (json['title'] ?? '') as String,
      groupId: (json['groupId'] ?? 0) as int,
      groupName: (json['groupName'] ?? '') as String,
      totalComments: (json['totalComments'] ?? 0) as int,
      discussions: discussions,
    );
  }

  Map<String, dynamic> toJson() {
    return {
      "id": id,
      "title": title,
      "groupId": groupId,
      "groupName": groupName,
      "totalComments": totalComments,
      "discussions": discussions,
    };
  }

  factory ForumModel.fromJson(source) => ForumModel.fromMap(source);
}

class DiscussionModel extends DiscussionEntity {
  const DiscussionModel(
      {required super.discussionId,
      required super.discussionTitle,
      required super.createdAt,
      required super.username,
      required super.name,
      required super.avatar,
      required super.imageUrl});

  factory DiscussionModel.fromMap(Map<String, dynamic> json) {
    return DiscussionModel(
      discussionId: (json['discussionId'] ?? 0) as int,
      discussionTitle: (json['discussionTitle'] ?? '') as String,
      createdAt: DateTime.parse(json['createdAt']),
      username: (json['username'] ?? '') as String,
      name: (json['name'] ?? '') as String,
      avatar: (json['avatar'] ?? '') as String,
      imageUrl: (json['imageUrl'] ?? '') as String,
    );
  }

  Map<String, dynamic> toJson() {
    return {
      "discussionId": discussionId,
      "discussionTitle": discussionTitle,
      "createdAt": createdAt,
      "username": username,
      "name": name,
      "avatar": avatar,
      "imageUrl": imageUrl,
    };
  }

  factory DiscussionModel.fromJson(source) => DiscussionModel.fromMap(source);
}