import 'package:dartz/dartz.dart';
import 'package:flutterapp/core/exceptions/failure.dart';
import 'package:flutterapp/core/usecases/posts/get_all_comment.dart';
import 'package:flutterapp/features/posts/domain/entities/comment_entity.dart';
import 'package:flutterapp/features/posts/domain/repository/discussion_repo.dart';

class GetAllCommentsUseCase
    implements GetAllCommentCore<List<CommentEntity>, ParamsCommentsBy> {
  final DiscussionRepo repository;

  GetAllCommentsUseCase({required this.repository});

  @override
  Future<Either<Failure, List<CommentEntity>>> call(
      ParamsCommentsBy params) async {
    return await repository.getAllCommentBy(params.discussionId);
  }
}