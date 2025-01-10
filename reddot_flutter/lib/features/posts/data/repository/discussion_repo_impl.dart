import 'dart:io';

import 'package:dartz/dartz.dart';
import 'package:flutterapp/core/exceptions/error.dart';
import 'package:flutterapp/core/exceptions/failure.dart';
import 'package:flutterapp/features/posts/data/data_sources/discussion_data_source.dart';
import 'package:flutterapp/features/posts/data/models/comment_model.dart';
import 'package:flutterapp/features/posts/domain/entities/dis_req_entity.dart';
import 'package:flutterapp/features/posts/domain/repository/discussion_repo.dart';

class DiscussionRepoImpl implements DiscussionRepo {
  final DiscussionDataSource discussionDataSource;

  DiscussionRepoImpl({required this.discussionDataSource});

  @override
  Future<Either<Failure, String>> createComment({
    required String content,
    required int discussionId,
    required String author,
  }) async {
    try {
      final result = await discussionDataSource.createComment(
          content, discussionId, author);
      if (result == null) return Left(ServerFailure());
      return Right(result);
    } on ServerException {
      return Left(ServerFailure());
    }
  }

  @override
  Future<Either<Failure, DiscussionResponseEntity>> createDiscussion({
    required String title,
    required String content,
    required int forumId,
    required String author,
  }) async {
    try {
      final result = await discussionDataSource.createDiscussion(
          title, content, forumId, author);
      if (result == null) return Left(ServerFailure());
      return Right(result);
    } on ServerException {
      return Left(ServerFailure());
    }
  }

  @override
  Future<Either<Failure, List<CommentModel>>> getAllCommentBy(
      int discussionId) async {
    try {
      final comments =
          await discussionDataSource.getAllCommentsBy(discussionId);
      return Right(comments);
    } on ServerException {
      return Left(ServerFailure());
    }
  }
}