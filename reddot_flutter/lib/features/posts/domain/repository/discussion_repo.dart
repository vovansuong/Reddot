import 'dart:io';

import 'package:dartz/dartz.dart';
import 'package:flutterapp/core/exceptions/failure.dart';
import 'package:flutterapp/features/posts/domain/entities/comment_entity.dart';
import 'package:flutterapp/features/posts/domain/entities/dis_req_entity.dart';

abstract class DiscussionRepo {
  Future<Either<Failure, List<CommentEntity>>> getAllCommentBy(
      int discussionId);

  Future<Either<Failure, DiscussionResponseEntity>> createDiscussion({
    required String title,
    required String content,
    required int forumId,
    required String author,
  });

  Future<Either<Failure, String>> createComment({
    required String content,
    required int discussionId,
    required String author,
  });
}