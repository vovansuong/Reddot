import 'package:dartz/dartz.dart';
import 'package:equatable/equatable.dart';
import 'package:flutterapp/core/exceptions/failure.dart';

abstract class GetUserInfoParams<Type, ParamsGetUserInfo> {
  Future<Either<Failure, Type>> call(ParamsGetUserInfo params);
}

class ParamsGetUserInfo extends Equatable {
  final String userId;

  const ParamsGetUserInfo({required this.userId});

  @override
  List<Object?> get props => [userId];
}

class NoParams extends Equatable {
  @override
  List<Object?> get props => [];
}