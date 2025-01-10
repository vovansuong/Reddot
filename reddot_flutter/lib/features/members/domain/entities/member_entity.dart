import 'package:equatable/equatable.dart';

class MemberEntity extends Equatable {
  final int userId;
  final String username;
  final String name;
  final String email;
  final String avatar;
  final String imageUrl;
  final int totalDiscussions;
  final int totalComments;
  final int reputation;
  final String status;

  const MemberEntity({
    required this.userId,
    required this.username,
    required this.name,
    required this.email,
    required this.avatar,
    required this.imageUrl,
    required this.totalDiscussions,
    required this.totalComments,
    required this.reputation,
    required this.status,
  });

  @override
  List<Object?> get props => [
        userId,
        username,
        name,
        email,
        avatar,
        imageUrl,
        totalDiscussions,
        totalComments,
        reputation,
        status,
      ];
}