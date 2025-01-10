part of 'auth_bloc.dart';

abstract class AuthState extends Equatable {
  @override
  List<Object> get props => [];
}

class Authenticated extends AuthState {}

class Unauthenticated extends AuthState {}

//login state
class LoginLoading extends AuthState {}

class LoginSuccess extends AuthState {
  final UserEntity userEntity;
  LoginSuccess({required this.userEntity});

  @override
  List<Object> get props => [userEntity];
}

class LoginFailure extends AuthState {
  final String message;
  LoginFailure({required this.message});

  @override
  List<Object> get props => [message];
}

//register state
class RegisterSuccess extends AuthState {}

class RegisterFailure extends AuthState {
  final String message;
  RegisterFailure({this.message = ''});
  @override
  List<Object> get props => [message];
}

class ProfPicLoading extends AuthState {}

class ProfPicSuccess extends AuthState {}