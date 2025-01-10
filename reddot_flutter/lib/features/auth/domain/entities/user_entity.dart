import 'dart:ffi';

import 'package:equatable/equatable.dart';
import 'package:formz/formz.dart';

class UserEntity extends Equatable {
  final int id;
  final String username;
  final String email;
  final String name;
  final String avatar;
  final String imageUrl;
  final String accessToken;

  const UserEntity(
      {required this.id,
      required this.username,
      required this.email,
      required this.name,
      required this.avatar,
      required this.imageUrl,
      required this.accessToken});

  @override
  List<Object?> get props => [
        id,
        username,
        email,
        name,
        avatar,
        imageUrl,
        accessToken,
      ];
}

//------------------------------------------------------------------------------
enum EmailValidationError { invalid, empty }

class Email extends FormzInput<String, EmailValidationError> {
  const Email.pure() : super.pure('');

  const Email.dirty([super.value = '']) : super.dirty();

  static final RegExp _emailRegExp = RegExp(
    r'^[a-zA-Z0-9.!#$%&’*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\.[a-zA-Z0-9-]+)*$',
  );

  @override
  EmailValidationError? validator(String value) {
    if (value.isEmpty) {
      return EmailValidationError.empty;
    } else if (!_emailRegExp.hasMatch(value)) {
      return EmailValidationError.invalid;
    }
    return null;
  }
}

//------------------------------------------------------------------------------

enum PasswordValidationError { invalid, empty }

class Password extends FormzInput<String, PasswordValidationError> {
  const Password.pure() : super.pure('');

  const Password.dirty([super.value = '']) : super.dirty();

  static final _passwordRegExp =
      RegExp(r'^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,}$');

  @override
  PasswordValidationError? validator(String value) {
    if (value.isEmpty) {
      return PasswordValidationError.empty;
    } else if (!_passwordRegExp.hasMatch(value ?? '')) {
      return PasswordValidationError.invalid;
    }
    return null;
  }
}

extension on EmailValidationError {
  String text() {
    switch (this) {
      case EmailValidationError.invalid:
        return 'Please ensure the email entered is valid';
      case EmailValidationError.empty:
        return 'Please enter an email';
    }
  }
}

extension on PasswordValidationError {
  String text() {
    switch (this) {
      case PasswordValidationError.invalid:
        return '''Password must be at least 8 characters and contain at least one letter and number''';
      case PasswordValidationError.empty:
        return 'Please enter a password';
    }
  }
}