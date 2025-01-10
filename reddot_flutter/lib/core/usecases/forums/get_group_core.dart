import 'package:dartz/dartz.dart';
import 'package:equatable/equatable.dart';
import 'package:flutterapp/core/exceptions/failure.dart';

abstract class GetAllGroupCore<Type, NoParams> {
  Future<Either<Failure, Type>> call(NoParams params);
}

class NoParams extends Equatable {
  @override
  List<Object?> get props => [];
}