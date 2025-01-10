import 'package:flutterapp/features/profile/domain/entities/user_pro_entity.dart';

class UserProModel extends UserProEntity {
  const UserProModel({
    required super.userId,
    required super.username,
    required super.name,
    required super.email,
    required super.phone,
    required super.avatar,
    required super.imageUrl,
    required super.status,
    required super.address,
    required super.reputation,
    required super.totalDiscussions,
    required super.totalComments,
    required super.totalFollowers,
    required super.totalFollowing,
    required super.bio,
    required super.birthDate,
    required super.gender,
    required super.comments,
  });

  factory UserProModel.fromJson(Map<dynamic, dynamic> json) {
    List jsonResponse = json["comments"] ?? [];
    List<CommentModel> comments =
        jsonResponse.map((e) => CommentModel.fromJson(e)).toList();

    return UserProModel(
      userId: (json['userId'] ?? 0) as int,
      username: (json['username'] ?? '') as String,
      name: (json['name'] ?? '') as String,
      email: (json['email'] ?? '') as String,
      phone: (json['phone'] ?? '') as String,
      avatar: (json['avatar'] ?? '') as String,
      imageUrl: (json['imageUrl'] ?? '') as String,
      status: (json['status'] ?? '') as String,
      address: (json['address'] ?? '') as String,
      reputation: (json['reputation'] ?? 0) as int,
      totalDiscussions: (json['totalDiscussions'] ?? 0) as int,
      totalComments: (json['totalComments'] ?? 0) as int,
      totalFollowers: (json['totalFollowers'] ?? 0) as int,
      totalFollowing: (json['totalFollowing'] ?? 0) as int,
      bio: (json['bio'] ?? '') as String,
      birthDate: json['birthDate'] != null
          ? DateTime.parse(json['birthDate'])
          : DateTime.now(),
      gender: (json['gender'] ?? '') as String,
      comments: comments,
    );
  }
}

class CommentModel extends CommentEntity {
  const CommentModel({
    required super.commentId,
    required super.author,
    required super.createdAt,
    required super.updatedAt,
    required super.discussionId,
    required super.discussionTitle,
    required super.content,
    required super.firstComment,
    required super.vote,
  });

  factory CommentModel.fromJson(Map<String, dynamic> json) {
    final vote = json['vote'] != null
        ? VoteModel.fromJson(json['vote'])
        : const VoteModel(id: -1, voteName: '', voteValue: 0);
    return CommentModel(
      commentId: (json['commentId'] ?? 0) as int,
      author: (json['author'] ?? "") as String,
      createdAt: DateTime.parse(json['createdAt']),
      updatedAt: DateTime.parse(json['updatedAt']),
      discussionId: (json['discussionId'] ?? 0) as int,
      discussionTitle: (json['discussionTitle'] ?? '') as String,
      content: (json['content'] ?? '') as String,
      firstComment: (json['firstComment'] != null ? true : false),
      vote: vote,
    );
  }
}

class VoteModel extends VoteEntity {
  const VoteModel({
    required super.id,
    required super.voteName,
    required super.voteValue,
  });

  factory VoteModel.fromJson(Map<dynamic, dynamic> json) {
    return VoteModel(
      id: (json['id'] ?? 0) as int,
      voteName: (json['voteName'] ?? '') as String,
      voteValue: (json['voteValue'] ?? 0) as int,
    );
  }
}