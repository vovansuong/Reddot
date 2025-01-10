import 'package:dartz/dartz.dart';
import 'package:flutterapp/core/exceptions/error.dart';
import 'package:flutterapp/core/exceptions/failure.dart';
import 'package:flutterapp/features/forums/data/models/forum_group_model.dart';
import 'package:flutterapp/features/forums/data/models/forum_model.dart';
import 'package:flutterapp/features/forums/domain/entities/discussion_entity.dart';
import 'package:flutterapp/features/forums/domain/entities/forum_entity.dart';
import 'package:flutterapp/features/forums/domain/repository/forum_repo.dart';

import '../data_sources/forum_data_source.dart';

class ForumRepoImpl implements ForumRepo {
  final ForumDataSource forumDataSource;

  ForumRepoImpl({required this.forumDataSource});

  @override
  Future<Either<Failure, List<ForumModel>>> getAllForum() async {
    try {
      final forums = await forumDataSource.getAllForum();
      return Right(forums);
    } on ServerException {
      return Left(ServerFailure());
    }
  }

  @override
  Future<Either<Failure, List<ForumsGroupModel>>> getAllGroup() async {
    try {
      final forumsGroup = await forumDataSource.getAllForumGroup();
      return Right(forumsGroup);
    } on ServerException {
      return Left(ServerFailure());
    }
  }

  @override
  Future<Either<Failure, List<ForumEntity>>> getForumByGroupId(
      int groupId) async {
    try {
      final forums = await forumDataSource.getAllForumByGroup(groupId);
      return Right(forums);
    } on ServerException {
      return Left(ServerFailure());
    }
  }

  @override
  Future<Either<Failure, List<DiscussionAllEntity>>> getAllDiscussion(
      String title) async {
    try {
      final discussions = await forumDataSource.getAllDiscussions(title);
      return Right(discussions);
    } on ServerException {
      return Left(ServerFailure());
    }
  }
}