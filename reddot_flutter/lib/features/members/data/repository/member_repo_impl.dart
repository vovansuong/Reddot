import 'package:dartz/dartz.dart';
import 'package:flutterapp/core/exceptions/error.dart';
import 'package:flutterapp/features/members/data/data_sources/member_data_source.dart';
import 'package:flutterapp/features/members/domain/entities/member_entity.dart';
import 'package:flutterapp/features/members/domain/repository/member_repo.dart';

import '../../../../core/exceptions/failure.dart';

class MemberRepoImpl implements MemberRepo {
  final MemberDataSource memberDataSource;

  MemberRepoImpl({required this.memberDataSource});

  @override
  Future<Either<Failure, List<MemberEntity>>> getAllMember() async {
    try {
      final members = await memberDataSource.getAllMember();
      return Right(members);
    } on ServerException {
      return Left(ServerFailure());
    }
  }

  @override
  Future<Either<Failure, List<MemberEntity>>> searchMember(String query) async {
    try {
      final members = await memberDataSource.searchMember(query);
      return Right(members);
    } on ServerException {
      return Left(ServerFailure());
    }
  }
}