import 'package:equatable/equatable.dart';

class DiscussionAllEntity extends Equatable {
  final int? id;
  final String? title;
  final DateTime? createdDate;
  final AuthorEntity? author;

  const DiscussionAllEntity({
    required this.id,
    required this.title,
    required this.createdDate,
    required this.author,
  });

  @override
  List<Object?> get props => [
        id,
        title,
        createdDate,
        author,
      ];
}

class AuthorEntity extends Equatable {
  final String? username;
  final String? avatar;
  final String? imageUrl;
  final String? badgeName;

  const AuthorEntity({
    required this.username,
    required this.avatar,
    required this.imageUrl,
    required this.badgeName,
  });

  @override
  List<Object?> get props => [username, avatar, imageUrl, badgeName];
}