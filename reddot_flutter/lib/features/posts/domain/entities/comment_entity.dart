import 'package:equatable/equatable.dart';

class CommentEntity extends Equatable {
  final int commentId;
  final AuthorEntity author;
  final DateTime createdAt;
  final DateTime updatedAt;

  final String title;
  final String content;
  final bool closed;
  final bool hidden;
  final bool firstComment;

  final List<ReplyEntity>? replies;

  CommentEntity({
    required this.commentId,
    required this.author,
    required this.createdAt,
    required this.updatedAt,
    required this.title,
    required this.content,
    required this.closed,
    required this.hidden,
    required this.firstComment,
    required this.replies,
  });

  @override
  List<Object?> get props => [
        commentId,
        author,
        createdAt,
        updatedAt,
        title,
        content,
        closed,
        hidden,
        firstComment,
        replies
      ];
}

class AuthorEntity extends Equatable {
  final String username;
  final String avatar;
  final String imageUrl;
  final int reputation;
  final int totalDiscussions;
  final int totalComments;

  const AuthorEntity({
    required this.username,
    required this.avatar,
    required this.imageUrl,
    required this.reputation,
    required this.totalDiscussions,
    required this.totalComments,
  });

  @override
  List<Object?> get props =>
      [username, avatar, imageUrl, reputation, totalDiscussions, totalComments];
}

class ReplyEntity extends Equatable {
  final int replyId;
  final AuthorEntity author;
  final DateTime createdAt;
  final String content;

  const ReplyEntity({
    required this.replyId,
    required this.author,
    required this.createdAt,
    required this.content,
  });

  @override
  List<Object?> get props => [replyId, author, createdAt, content];
}