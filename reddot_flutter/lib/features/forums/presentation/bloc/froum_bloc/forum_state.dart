part of 'forum_bloc.dart';

enum ForumEventStatus { initial, loading, success, failure }

abstract class ForumState extends Equatable {
  const ForumState();
  @override
  List<Object> get props => [];
}

final class ForumLoading extends ForumState {}

class ForumSuccess extends ForumState {
  final List<ForumEntity> forums;

  const ForumSuccess({this.forums = const <ForumEntity>[]});

  @override
  List<Object> get props => [forums];
}

class ForumFailure extends ForumState {
  final String message;
  const ForumFailure({required this.message});
}