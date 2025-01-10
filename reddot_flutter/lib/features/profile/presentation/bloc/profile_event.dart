part of 'profile_bloc.dart';

abstract class ProfileEvent extends Equatable {
  const ProfileEvent();
  @override
  List<Object> get props => [];
}

class ProfileStartEvent extends ProfileEvent {}

class GetProfileEvent extends ProfileEvent {
  final String username;

  const GetProfileEvent({required this.username});

  @override
  List<Object> get props => [username];
}

class UpdateProfileEvent extends ProfileEvent {
  final ParamsEditUserPro userPro;

  const UpdateProfileEvent({required this.userPro});

  @override
  List<Object> get props => [userPro];
}