import 'package:dartz/dartz.dart';
import 'package:equatable/equatable.dart';
import 'package:flutterapp/core/exceptions/failure.dart';

abstract class UpdateInfoCore<Type, ParamsEditUserPro> {
  Future<Either<Failure, Type>> call(ParamsEditUserPro params);
}

class ParamsEditUserPro extends Equatable {
  final String? username;
  final String? email;
  final String? name;
  final String? phone;
  final String? address;
  final DateTime? birthday;
  final String? gender;
  final String? bio;

  const ParamsEditUserPro({
    required this.username,
    required this.name,
    required this.email,
    required this.phone,
    required this.gender,
    required this.bio,
    required this.address,
    required this.birthday,
  });

  @override
  List<Object?> get props =>
      [username, name, email, phone, gender, bio, address, birthday];
}