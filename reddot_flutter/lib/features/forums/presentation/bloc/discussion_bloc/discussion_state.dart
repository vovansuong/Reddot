part of 'discussion_bloc.dart';

abstract class DiscussionState extends Equatable {
  const DiscussionState();
  @override
  List<Object> get props => [];
}

final class DiscussionLoading extends DiscussionState {}

class DiscussionSuccess extends DiscussionState {
  final List<DiscussionAllEntity> discussions;
  final String search;

  const DiscussionSuccess({
    this.discussions = const <DiscussionAllEntity>[],
    this.search = "",
  });

  @override
  List<Object> get props => [discussions, search];
}

class DiscussionFailure extends DiscussionState {
  final String message;
  const DiscussionFailure({required this.message});
}