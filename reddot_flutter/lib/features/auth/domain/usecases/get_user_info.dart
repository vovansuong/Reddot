import 'package:dartz/dartz.dart';
import 'package:flutterapp/core/exceptions/failure.dart';
import 'package:flutterapp/core/usecases/get_user_info.dart';
import 'package:flutterapp/features/auth/domain/entities/user_entity.dart';

import '../repository/auth_repo.dart';

class GetUserInfoUseCase
    implements GetUserInfoParams<UserEntity, ParamsGetUserInfo> {
  final AuthRepo repository;

  GetUserInfoUseCase({required this.repository});

  @override
  Future<Either<Failure, UserEntity>> call(ParamsGetUserInfo params) async {
    return await repository.getUserInfo(params.userId);
  }
}