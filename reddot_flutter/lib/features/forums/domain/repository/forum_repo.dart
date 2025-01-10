import 'package:dartz/dartz.dart';
import 'package:flutterapp/core/exceptions/failure.dart';
import 'package:flutterapp/features/forums/domain/entities/discussion_entity.dart';
import 'package:flutterapp/features/forums/domain/entities/forum_group_entity.dart';

import '../entities/forum_entity.dart';

abstract class ForumRepo {
  Future<Either<Failure, List<ForumGroupEntity>>> getAllGroup();

  Future<Either<Failure, List<ForumEntity>>> getAllForum();

  Future<Either<Failure, List<ForumEntity>>> getForumByGroupId(int groupId);

  Future<Either<Failure, List<DiscussionAllEntity>>> getAllDiscussion(
      String title);
}