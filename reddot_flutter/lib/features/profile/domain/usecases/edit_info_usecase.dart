import 'package:dartz/dartz.dart';
import 'package:flutterapp/core/exceptions/failure.dart';
import 'package:flutterapp/core/usecases/profile/update_info_core.dart';
import 'package:flutterapp/features/profile/domain/entities/user_pro_entity.dart';
import 'package:flutterapp/features/profile/domain/repository/profile_repo.dart';

class UpdateInfoUseCase
    implements UpdateInfoCore<UserProEntity, ParamsEditUserPro> {
  final ProfileRepo repository;

  UpdateInfoUseCase({required this.repository});

  @override
  Future<Either<Failure, UserProEntity>> call(ParamsEditUserPro params) async {
    return await repository.updateInfo(params);
  }
}