import 'package:flutterapp/features/forums/domain/entities/discussion_entity.dart';

class DiscussionAllModel extends DiscussionAllEntity {
  const DiscussionAllModel({
    super.id,
    super.title,
    super.createdDate,
    super.author,
  });

  factory DiscussionAllModel.fromMap(Map<String, dynamic> json) {
    final jsonAuthor = json['author'] ?? {};
    AuthorModel author = AuthorModel.fromMap(jsonAuthor);
    return DiscussionAllModel(
      id: (json['id'] ?? 0) as int,
      title: (json['title'] ?? '') as String,
      createdDate: DateTime.parse(json['createdDate']),
      author: author,
    );
  }

  factory DiscussionAllModel.fromJson(source) =>
      DiscussionAllModel.fromMap(source);
}

class AuthorModel extends AuthorEntity {
  const AuthorModel({
    required super.username,
    required super.avatar,
    required super.imageUrl,
    required super.badgeName,
  });

  factory AuthorModel.fromMap(Map<dynamic, dynamic> json) {
    return AuthorModel(
      username: (json['username'] ?? '') as String,
      avatar: (json['avatar'] ?? '') as String,
      imageUrl: (json['imageUrl'] ?? '') as String,
      badgeName: (json['badgeName'] ?? '') as String,
    );
  }

  factory AuthorModel.fromJson(source) => AuthorModel.fromMap(source);
}