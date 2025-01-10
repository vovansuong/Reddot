import 'package:dartz/dartz.dart';
import 'package:equatable/equatable.dart';

import '../../exceptions/failure.dart';

abstract class GetAllDiscussion<Type, ParamsGetDiscussionTitle> {
  Future<Either<Failure, Type>> call(ParamsGetDiscussionTitle params);
}

class ParamsGetDiscussionTitle extends Equatable {
  final String title;

  const ParamsGetDiscussionTitle({required this.title});

  @override
  List<Object?> get props => [title];
}

class NoParams extends Equatable {
  @override
  List<Object?> get props => [];
}
