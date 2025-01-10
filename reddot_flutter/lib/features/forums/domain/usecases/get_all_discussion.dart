import 'package:dartz/dartz.dart';
import 'package:flutterapp/core/exceptions/failure.dart';
import 'package:flutterapp/core/usecases/search/get_all_discussion.dart';
import 'package:flutterapp/features/forums/domain/entities/discussion_entity.dart';
import 'package:flutterapp/features/forums/domain/repository/forum_repo.dart';

class GetAllDiscussionCase
    implements
        GetAllDiscussion<List<DiscussionAllEntity>, ParamsGetDiscussionTitle> {
  final ForumRepo repository;

  GetAllDiscussionCase({required this.repository});

  @override
  Future<Either<Failure, List<DiscussionAllEntity>>> call(
      ParamsGetDiscussionTitle params) async {
    return await repository.getAllDiscussion(params.title);
  }
}