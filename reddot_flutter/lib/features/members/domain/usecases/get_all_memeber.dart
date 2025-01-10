import 'package:dartz/dartz.dart';
import 'package:flutterapp/core/exceptions/failure.dart';
import 'package:flutterapp/core/usecases/members/get_all_member.dart';
import 'package:flutterapp/features/members/domain/entities/member_entity.dart';

import '../repository/member_repo.dart';

class GetAllMemberUseCase
    implements GetAllMember<List<MemberEntity>, NoParams> {
  final MemberRepo repository;

  GetAllMemberUseCase({required this.repository});

  @override
  Future<Either<Failure, List<MemberEntity>>> call(NoParams params) async {
    return await repository.getAllMember();
  }
}