import 'package:dartz/dartz.dart';
import 'package:flutterapp/core/exceptions/failure.dart';
import 'package:flutterapp/core/usecases/posts/create_comment.dart';
import 'package:flutterapp/features/posts/domain/entities/comment_entity.dart';
import 'package:flutterapp/features/posts/domain/repository/discussion_repo.dart';

class CreateCommentUseCase implements CreateCommentParams<void, ParamsComment> {
  final DiscussionRepo repository;

  CreateCommentUseCase({required this.repository});

  @override
  Future<Either<Failure, String>> call(ParamsComment params) async {
    return await repository.createComment(
        content: params.content,
        discussionId: params.discussionId,
        author: params.author);
  }
}