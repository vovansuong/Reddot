part of 'discussion_bloc.dart';

abstract class DiscussionEvent extends Equatable {
  const DiscussionEvent();
  @override
  List<Object> get props => [];
}

class GetAllDiscussionsEvent extends DiscussionEvent {
  final String search;

  const GetAllDiscussionsEvent({this.search = ''});

  @override
  List<Object> get props => [search];
}

class GetDiscussionsComplete extends DiscussionEvent {}