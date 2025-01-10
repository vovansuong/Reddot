import 'package:dartz/dartz.dart';
import 'package:equatable/equatable.dart';

import '../exceptions/failure.dart';


abstract class RegisterUserParams<Type, ParamsRegister> {
  Future<Either<Failure, Type>> call(ParamsRegister params);
}

class ParamsRegister extends Equatable {
  final String email;
  final String password;
  final String username;

  const ParamsRegister({
    required this.username,
    required this.email,
    required this.password,
  });

  @override
  List<Object?> get props => [email, username, password];
}