import 'package:dartz/dartz.dart';
import 'package:flutterapp/core/exceptions/failure.dart';
import 'package:flutterapp/core/usecases/forums/get_all_forum.dart';
import 'package:flutterapp/features/forums/domain/repository/forum_repo.dart';

import '../entities/forum_entity.dart';

class GetAllForumUseCase
    implements GetAllForumCore<List<ForumEntity>, NoParams> {
  final ForumRepo repository;

  GetAllForumUseCase({required this.repository});

  @override
  Future<Either<Failure, List<ForumEntity>>> call(NoParams params) async {
    return await repository.getAllForum();
  }
}