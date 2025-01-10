part of 'forum_filter_bloc.dart';

abstract class ForumFilterEvent extends Equatable {
  const ForumFilterEvent();

  @override
  List<Object> get props => [];
}

class UpdateFilter extends ForumFilterEvent {
  const UpdateFilter();

  @override
  List<Object> get props => [];
}

class UpdateForums extends ForumFilterEvent {
  final int forumFilter;

  const UpdateForums({this.forumFilter = -1});

  @override
  List<Object> get props => [forumFilter];
}
