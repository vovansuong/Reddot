import 'package:dartz/dartz.dart';
import 'package:flutterapp/core/exceptions/error.dart';
import 'package:flutterapp/core/exceptions/failure.dart';
import 'package:flutterapp/core/usecases/profile/update_info_core.dart';
import 'package:flutterapp/features/profile/data/data_sources/profile_data_source.dart';
import 'package:flutterapp/features/profile/data/models/user_pro_model.dart';
import 'package:flutterapp/features/profile/domain/entities/user_pro_entity.dart';
import 'package:flutterapp/features/profile/domain/repository/profile_repo.dart';

class ProfileRepoImpl extends ProfileRepo {
  final ProfileDataSource profileDataSource;

  ProfileRepoImpl({required this.profileDataSource});

  @override
  Future<Either<Failure, UserProModel>> getUserProBy(String username) async {
    try {
      final userPro = await profileDataSource.getUserProBy(username);
      return Right(userPro);
    } on ServerException {
      return Left(ServerFailure());
    }
  }

  @override
  Future<Either<Failure, UserProEntity>> updateInfo(
      ParamsEditUserPro params) async{
    try {
      final userPro = await profileDataSource.updateInfo(params);
      return Right(userPro);
    } on ServerException {
      return Left(ServerFailure());
    }
  }
}