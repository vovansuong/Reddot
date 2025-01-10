import 'package:dartz/dartz.dart';
import 'package:equatable/equatable.dart';

import '../../exceptions/failure.dart';

abstract class GetAllCommentCore<Type, ParamsCommentsBy> {
  Future<Either<Failure, Type>> call(ParamsCommentsBy params);
}

class ParamsCommentsBy extends Equatable {
  final int discussionId;
  const ParamsCommentsBy({
    required this.discussionId,
  });

  @override
  List<Object?> get props => [discussionId];
}