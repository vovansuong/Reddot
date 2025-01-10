import 'package:dartz/dartz.dart';
import 'package:equatable/equatable.dart';
import 'package:flutterapp/core/exceptions/failure.dart';

abstract class GetUserProParams<Type, ParamsGetUserPro> {
  Future<Either<Failure, Type>> call(ParamsGetUserPro params);
}

class ParamsGetUserPro extends Equatable {
  final String username;

  const ParamsGetUserPro({required this.username});

  @override
  List<Object?> get props => [username];
}

class NoParams extends Equatable {
  @override
  List<Object?> get props => [];
}