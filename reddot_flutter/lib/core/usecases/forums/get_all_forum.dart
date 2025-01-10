import 'package:dartz/dartz.dart';
import 'package:equatable/equatable.dart';

import '../../exceptions/failure.dart';

abstract class GetAllForumCore<Type, NoParams> {
  Future<Either<Failure, Type>> call(NoParams params);
}

class NoParams extends Equatable {
  @override
  List<Object?> get props => [];
}