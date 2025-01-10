import 'package:flutterapp/features/posts/domain/entities/comment_entity.dart';

class CommentModel extends CommentEntity {
  CommentModel({
    required super.commentId,
    required super.author,
    required super.createdAt,
    required super.updatedAt,
    required super.title,
    required super.content,
    required super.closed,
    required super.hidden,
    required super.firstComment,
    required super.replies,
  });

  factory CommentModel.fromJson(Map<dynamic, dynamic> json) {
    List jsonResponse = json["replies"] ?? [];
    List<ReplyModel> replies =
        jsonResponse.map((e) => ReplyModel.fromJson(e)).toList();

    return CommentModel(
        commentId: (json['commentId'] ?? 0) as int,
        author: AuthorModel.fromJson(json['author']),
        createdAt: DateTime.parse(json['createdAt']),
        updatedAt: DateTime.parse(json['updatedAt']),
        title: (json['title'] ?? '') as String,
        content: (json['content'] ?? '') as String,
        closed: (json['closed'] ?? false) as bool,
        hidden: (json['hidden'] ?? false) as bool,
        firstComment: (json['firstComment'] ?? false) as bool,
        replies: []);
  }
}

class AuthorModel extends AuthorEntity {
  const AuthorModel({
    required super.username,
    required super.avatar,
    required super.imageUrl,
    required super.reputation,
    required super.totalDiscussions,
    required super.totalComments,
  });

  factory AuthorModel.fromJson(Map<String, dynamic> json) {
    return AuthorModel(
      username: (json['username'] ?? '') as String,
      avatar: (json['avatar'] ?? '') as String,
      imageUrl: (json['imageUrl'] ?? '') as String,
      reputation: (json['reputation'] ?? 0) as int,
      totalDiscussions: (json['totalDiscussions'] ?? 0) as int,
      totalComments: (json['totalComments'] ?? 0) as int,
    );
  }
}

class ReplyModel extends ReplyEntity {
  const ReplyModel({
    required super.replyId,
    required super.author,
    required super.createdAt,
    required super.content,
  });

  factory ReplyModel.fromJson(Map<dynamic, dynamic> json) {
    return ReplyModel(
        replyId: (json['replyId'] ?? 0) as int,
        author: AuthorModel.fromJson(json['author']),
        createdAt: DateTime.parse(json['createdAt']),
        content: (json['content'] ?? '') as String);
  }
}