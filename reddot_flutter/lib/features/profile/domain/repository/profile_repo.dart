import 'package:dartz/dartz.dart';
import 'package:flutterapp/core/exceptions/failure.dart';
import 'package:flutterapp/core/usecases/profile/update_info_core.dart';
import 'package:flutterapp/features/profile/domain/entities/user_pro_entity.dart';

abstract class ProfileRepo {
  Future<Either<Failure, UserProEntity>> getUserProBy(String username);

  Future<Either<Failure, UserProEntity>> updateInfo(ParamsEditUserPro params);
}