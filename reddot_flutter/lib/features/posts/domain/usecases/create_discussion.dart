import 'package:dartz/dartz.dart';
import 'package:flutterapp/core/exceptions/failure.dart';
import 'package:flutterapp/features/posts/domain/entities/dis_req_entity.dart';
import 'package:flutterapp/features/posts/domain/repository/discussion_repo.dart';

import '../../../../core/usecases/posts/create_discussion.dart';

class CreateDiscussionUseCase
    implements CreateDiscussionParams<void, ParamsDiscussion> {
  final DiscussionRepo repository;

  CreateDiscussionUseCase({required this.repository});

  @override
  Future<Either<Failure, DiscussionResponseEntity>> call(
      ParamsDiscussion params) async {
    return await repository.createDiscussion(
      title: params.title,
      content: params.content,
      forumId: params.forumId,
      author: params.author,
    );
  }
}