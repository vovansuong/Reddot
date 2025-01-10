import 'package:dartz/dartz.dart';
import 'package:equatable/equatable.dart';
import 'package:flutterapp/core/exceptions/failure.dart';

abstract class GetForumByParamsCore<Type, ParamsGetForumBy> {
  Future<Either<Failure, Type>> call(ParamsGetForumBy params);
}

class ParamsGetForumBy extends Equatable {
  final int groupId;

  const ParamsGetForumBy({required this.groupId});

  @override
  List<Object?> get props => [groupId];
}