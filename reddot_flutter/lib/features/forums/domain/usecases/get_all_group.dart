import 'package:dartz/dartz.dart';
import 'package:flutterapp/core/exceptions/failure.dart';
import 'package:flutterapp/core/usecases/forums/get_group_core.dart';
import 'package:flutterapp/features/forums/domain/entities/forum_group_entity.dart';
import 'package:flutterapp/features/forums/domain/repository/forum_repo.dart';

class GetAllGroupsUseCase
    implements GetAllGroupCore<List<ForumGroupEntity>, NoParams> {
  final ForumRepo repository;

  GetAllGroupsUseCase({required this.repository});

  @override
  Future<Either<Failure, List<ForumGroupEntity>>> call(NoParams params) async {
    return await repository.getAllGroup();
  }
}