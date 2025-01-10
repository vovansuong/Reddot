import 'package:equatable/equatable.dart';

class DiscussionResponseEntity extends Equatable {
  final int discussionId;
  final String discussionTitle;
  final DateTime createdAt;
  final String username;
  final String name;
  final String imageUrl;
  final String avatar;

  const DiscussionResponseEntity({
    required this.discussionId,
    required this.discussionTitle,
    required this.createdAt,
    required this.username,
    required this.name,
    required this.imageUrl,
    required this.avatar,
  });

  @override
  List<Object?> get props => [
        discussionId,
        discussionTitle,
        createdAt,
        username,
        name,
        imageUrl,
        avatar
      ];
}