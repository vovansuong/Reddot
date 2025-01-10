import 'package:dartz/dartz.dart';
import 'package:equatable/equatable.dart';
import 'package:flutterapp/core/exceptions/failure.dart';

abstract class SearchMemberCore<Type, ParamsSearch> {
  Future<Either<Failure, Type>> call(ParamsSearch params);
}

class ParamsSearch extends Equatable {
  final String query;

  const ParamsSearch({required this.query});

  @override
  List<Object?> get props => [query];
}