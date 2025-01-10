import 'package:equatable/equatable.dart';

class UserProEntity extends Equatable {
  final int userId;
  final String? username;
  final String? name;
  final String? email;
  final String? avatar;
  final String? imageUrl;
  final String? status;
  final String? address;

  final int? reputation;
  final int? totalDiscussions;
  final int? totalComments;
  final int? totalFollowers;
  final int? totalFollowing;

  final String? phone;
  final String? bio;
  final String? gender;
  final DateTime birthDate;

  final List<CommentEntity>? comments;

  const UserProEntity({
    required this.userId,
    required this.username,
    required this.name,
    required this.email,
    required this.phone,
    required this.avatar,
    required this.imageUrl,
    required this.status,
    required this.address,
    required this.reputation,
    required this.totalDiscussions,
    required this.totalComments,
    required this.totalFollowers,
    required this.totalFollowing,
    required this.bio,
    required this.birthDate,
    required this.gender,
    required this.comments,
  });

  @override
  List<Object?> get props => [
        userId,
        username,
        name,
        email,
        phone,
        avatar,
        imageUrl,
        status,
        address,
        reputation,
        totalDiscussions,
        totalComments,
        totalFollowers,
        totalFollowing,
        bio,
        birthDate,
        gender,
        comments,
      ];
}

class CommentEntity extends Equatable {
  final int commentId;
  final String author;
  final DateTime createdAt;
  final DateTime updatedAt;
  final int discussionId;
  final String discussionTitle;
  final String content;
  final bool? firstComment;
  final VoteEntity vote;

  const CommentEntity({
    required this.commentId,
    required this.author,
    required this.createdAt,
    required this.updatedAt,
    required this.discussionId,
    required this.discussionTitle,
    required this.content,
    required this.firstComment,
    required this.vote,
  });

  @override
  List<Object?> get props => [
        commentId,
        author,
        createdAt,
        updatedAt,
        discussionId,
        discussionTitle,
        content,
        firstComment,
        vote,
      ];
}

class VoteEntity extends Equatable {
  final int id;
  final String voteName;
  final int voteValue;

  const VoteEntity({
    required this.id,
    required this.voteName,
    required this.voteValue,
  });

  @override
  List<Object?> get props => [id, voteName, voteValue];
}