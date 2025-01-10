import 'package:dartz/dartz.dart';

import '../../../../core/exceptions/failure.dart';
import '../../../../core/usecases/register_user_core.dart';
import '../repository/auth_repo.dart';

class RegisterUser implements RegisterUserParams<String, ParamsRegister> {
  final AuthRepo repository;

  RegisterUser({required this.repository});

  @override
  Future<Either<Failure, String>> call(ParamsRegister params) async {
    return await repository.handleRegister(
      email: params.email,
      password: params.password,
      username: params.username,
    );
  }
}