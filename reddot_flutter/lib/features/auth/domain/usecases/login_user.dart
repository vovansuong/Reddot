import 'package:dartz/dartz.dart';
import 'package:flutterapp/features/auth/domain/repository/auth_repo.dart';

import '../../../../core/exceptions/failure.dart';
import '../../../../core/usecases/login_user_core.dart';

class LoginUser implements LoginUserParams<String, Params> {
  final AuthRepo repository;

  LoginUser({required this.repository});

  @override
  Future<Either<Failure, String>> call(Params params) async {
    return await repository.handleLogin(
      email: params.email,
      password: params.password,
    );
  }
}