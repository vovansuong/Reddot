import 'dart:io';

import 'package:dartz/dartz.dart';
import 'package:flutterapp/core/exceptions/error.dart';
import 'package:flutterapp/core/exceptions/failure.dart';
import 'package:flutterapp/features/auth/data/data_sources/auth_data_source.dart';
import 'package:flutterapp/features/auth/data/models/user_model.dart';
import 'package:flutterapp/features/auth/domain/repository/auth_repo.dart';

class AuthRepoImpl implements AuthRepo {
  final AuthDataSource authDataSource;
  AuthRepoImpl({required this.authDataSource});

  @override
  Future<Either<Failure, String>> handleRegister(
      {required String email,
      required String password,
      required String username}) async {
    try {
      final response =
          await authDataSource.handleRegister(email, password, username);
      return Right(response);
    } on ServerException catch (e) {
      return Left(ServerFailure());
    }
  }

  @override
  Future<Either<Failure, String>> handleLogin(
      {required String email, required String password}) async {
    try {
      final response = await authDataSource.handleLogin(email, password);
      return Right(response);
    } on ServerException catch (e) {
      return Left(ServerFailure());
    }
  }

  Future<void> logout() {
    throw UnimplementedError();
  }

  @override
  Future<Either<Failure, UserModel>> getUserInfo(String ownerId) async {
    try {
      final response = await authDataSource.getUserInfo(ownerId);
      return Right(response);
    } on ServerException catch (e) {
      return Left(ServerFailure());
    }
  }

  @override
  Future<Either<Failure, String>> changeProfImg({required File file}) async {
    try {
      final response = await authDataSource.changeProfilePic(file);
      return Right(response);
    } on ServerException catch (err) {
      return Left(ServerFailure());
    }
  }
}