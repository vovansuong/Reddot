import 'dart:io';

import 'package:dartz/dartz.dart';
import 'package:flutterapp/features/auth/domain/entities/user_entity.dart';
import '../../../../core/exceptions/failure.dart';

abstract class AuthRepo {
  Future<Either<Failure, String>> handleLogin({
    required String email,
    required String password,
  });

  Future<Either<Failure, String>> handleRegister({
    required String email,
    required String password,
    required String username,
  });

  Future<Either<Failure, UserEntity>> getUserInfo(String ownerId);

  Future<Either<Failure, String>> changeProfImg({
    required File file,
  });
}