import 'package:dartz/dartz.dart';
import 'package:flutterapp/core/exceptions/failure.dart';
import 'package:flutterapp/core/usecases/posts/get_all_comment.dart';
import 'package:flutterapp/core/usecases/profile/get_user_pro.dart';
import 'package:flutterapp/features/profile/domain/entities/user_pro_entity.dart';
import 'package:flutterapp/features/profile/domain/repository/profile_repo.dart';

class GetUserProUseCase
    implements GetAllCommentCore<UserProEntity, ParamsGetUserPro> {
  final ProfileRepo repository;

  GetUserProUseCase({required this.repository});

  @override
  Future<Either<Failure, UserProEntity>> call(ParamsGetUserPro params) async {
    return await repository.getUserProBy(params.username);
  }
}