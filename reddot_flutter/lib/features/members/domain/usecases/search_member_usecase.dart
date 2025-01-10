import 'package:dartz/dartz.dart';
import 'package:flutterapp/core/exceptions/failure.dart';
import 'package:flutterapp/core/usecases/members/search_memeber.dart';
import 'package:flutterapp/features/members/domain/entities/member_entity.dart';
import 'package:flutterapp/features/members/domain/repository/member_repo.dart';

class SearchMemberUseCase
    implements SearchMemberCore<List<MemberEntity>, ParamsSearch> {
  final MemberRepo repository;

  SearchMemberUseCase({required this.repository});

  @override
  Future<Either<Failure, List<MemberEntity>>> call(ParamsSearch params) async {
    return await repository.searchMember(params.query);
  }
}