import 'dart:ffi';

import 'package:equatable/equatable.dart';

class ForumEntity extends Equatable {
  final int? id;
  final String? title;
  final int? groupId;
  final String? groupName;
  final int? totalComments;
  final List<DiscussionEntity> discussions;

  const ForumEntity({
    required this.id,
    required this.title,
    required this.groupId,
    required this.groupName,
    required this.totalComments,
    required this.discussions,
  });

  @override
  List<Object?> get props =>
      [id, title, groupId, groupName, totalComments, discussions];
}

class DiscussionEntity extends Equatable {
  final int? discussionId;
  final String? discussionTitle;
  final DateTime? createdAt;
  final String? username;
  final String? name;
  final String? avatar;
  final String? imageUrl;

  const DiscussionEntity({
    required this.discussionId,
    required this.discussionTitle,
    required this.createdAt,
    required this.username,
    required this.name,
    required this.avatar,
    required this.imageUrl,
  });

  @override
  List<Object?> get props => [
        discussionId,
        discussionTitle,
        createdAt,
        username,
        name,
        avatar,
        imageUrl
      ];
}