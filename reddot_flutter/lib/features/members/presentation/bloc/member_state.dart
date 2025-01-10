part of 'member_bloc.dart';

enum MemberEventStatus { initial, loading, success, failure }

abstract class MemberState extends Equatable {
  const MemberState();

  @override
  List<Object> get props => [];
}

class MemberLoading extends MemberState {}

class MemberSuccess extends MemberState {
  final List<MemberEntity> members;
  final String search;

  const MemberSuccess({
    this.members = const <MemberEntity>[],
    this.search = '',
  });
}

class MemberFailure extends MemberState {
  final String message;

  const MemberFailure({this.message = ''});

  @override
  List<Object> get props => [message];
}