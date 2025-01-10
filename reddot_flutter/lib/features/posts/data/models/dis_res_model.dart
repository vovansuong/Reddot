import 'package:flutterapp/features/posts/domain/entities/dis_req_entity.dart';

class DiscussionResponseModel extends DiscussionResponseEntity {
  const DiscussionResponseModel({
    required super.discussionId,
    required super.discussionTitle,
    required super.createdAt,
    required super.username,
    required super.name,
    required super.avatar,
    required super.imageUrl,
  });

  factory DiscussionResponseModel.fromMap(Map<dynamic, dynamic> json) {
    return DiscussionResponseModel(
      discussionId: (json['discussionId'] ?? 0) as int,
      discussionTitle: (json['discussionTitle'] ?? '') as String,
      createdAt: DateTime.parse(json['createdAt']),
      username: (json['username'] ?? '') as String,
      name: (json['name'] ?? '') as String,
      avatar: (json['avatar'] ?? '') as String,
      imageUrl: (json['imageUrl'] ?? '') as String,
    );
  }
}