part of 'auth_bloc.dart';

abstract class AuthEvent extends Equatable {
  @override
  List<Object> get props => [];
}

class AppStarted extends AuthEvent {}

class LoggedIn extends AuthEvent {
  final String email;
  final String password;

  LoggedIn({
    required this.email,
    required this.password,
  });

  @override
  List<Object> get props => [email, password];
}

class LoggedOut extends AuthEvent {}

class Register extends AuthEvent {
  final String email;
  final String password;
  final String username;

  Register({
    required this.email,
    required this.password,
    required this.username,
  });

  @override
  List<Object> get props => [email, password, username];
}

class ChangeProfPic extends AuthEvent {
  final File file;

  ChangeProfPic(this.file);

  @override
  List<Object> get props => [file];
}