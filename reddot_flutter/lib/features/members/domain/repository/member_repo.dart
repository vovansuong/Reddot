import 'package:dartz/dartz.dart';

import '../../../../core/exceptions/failure.dart';
import '../entities/member_entity.dart';

abstract class MemberRepo {
  Future<Either<Failure, List<MemberEntity>>> getAllMember();
  Future<Either<Failure, List<MemberEntity>>> searchMember(String query);
}