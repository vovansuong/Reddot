part of 'forum_bloc.dart';

abstract class ForumEvent extends Equatable {
  const ForumEvent();
  @override
  List<Object> get props => [];
}

class GetAllForumsEvent extends ForumEvent {
  final List<ForumEntity> forums;

  const GetAllForumsEvent({this.forums = const <ForumEntity>[]});

  @override
  List<Object> get props => [forums];
}

class GetForumsComplete extends ForumEvent {}