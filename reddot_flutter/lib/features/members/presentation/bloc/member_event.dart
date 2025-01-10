part of 'member_bloc.dart';

abstract class MemberEvent extends Equatable {
  const MemberEvent();

  @override
  List<Object> get props => [];
}

class GetMemberEvent extends MemberEvent {}

class SearchMemberEvent extends MemberEvent {
  final String query;

  const SearchMemberEvent({this.query = ''});

  @override
  List<Object> get props => [query];
}

class MemberRequestCompleted extends MemberEvent {}