import 'package:equatable/equatable.dart';

class DiscussionEntity extends Equatable {
  final int? id;
  final String? authorId;
  final String? title;
  final String? content;

  final int? forumId;
  final String? forumName;
  final String? createdAt;
  final String? updatedAt;

  const DiscussionEntity({
    required this.id,
    required this.authorId,
    required this.title,
    required this.content,
    required this.forumId,
    required this.forumName,
    required this.createdAt,
    required this.updatedAt,
  });

  @override
  List<Object?> get props =>
      [id, authorId, title, content, forumId, forumName, createdAt, updatedAt];
}