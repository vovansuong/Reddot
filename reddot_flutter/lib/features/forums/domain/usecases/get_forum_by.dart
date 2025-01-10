import 'package:dartz/dartz.dart';
import 'package:flutterapp/core/exceptions/failure.dart';
import 'package:flutterapp/core/usecases/forums/get_forum_by.dart';
import 'package:flutterapp/features/forums/domain/entities/forum_entity.dart';
import 'package:flutterapp/features/forums/domain/repository/forum_repo.dart';

class GetForumByParamsUseCase
    implements GetForumByParamsCore<List<ForumEntity>, ParamsGetForumBy> {
  final ForumRepo repository;

  GetForumByParamsUseCase({required this.repository});

  @override
  Future<Either<Failure, List<ForumEntity>>> call(
      ParamsGetForumBy params) async {
    return await repository.getForumByGroupId(params.groupId);
  }
}