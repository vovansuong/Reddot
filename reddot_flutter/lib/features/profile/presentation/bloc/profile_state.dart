part of 'profile_bloc.dart';

abstract class ProfileState extends Equatable {
  const ProfileState();
  @override
  List<Object> get props => [];
}

class ProfileInitial extends ProfileState {}

class ProfileLoading extends ProfileState {}

class ProfileLoaded extends ProfileState {
  final UserProEntity userPro;

  const ProfileLoaded({required this.userPro});

  @override
  List<Object> get props => [userPro];
}

class ProfileError extends ProfileState {}

class ProfileUpdated extends ProfileState {
  final UserProEntity userPro;

  const ProfileUpdated({required this.userPro});

  @override
  List<Object> get props => [userPro];
}