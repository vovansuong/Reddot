part of 'comments_bloc.dart';

abstract class CommentsEvent extends Equatable {
  const CommentsEvent();
  @override
  List<Object> get props => [];
}

class LoadCommentsEvent extends CommentsEvent {
  final int discussionId;

  const LoadCommentsEvent({this.discussionId = 1});

  @override
  List<Object> get props => [discussionId];
}

class AddCommentEvent extends CommentsEvent {
  final int discussionId;
  final String content;

  const AddCommentEvent({required this.discussionId, required this.content});

  @override
  List<Object> get props => [discussionId, content];
}

class AddDiscussionEvent extends CommentsEvent {
  final String title;
  final String content;
  final int forumId;

  const AddDiscussionEvent({
    required this.title,
    required this.content,
    required this.forumId,
  });

  @override
  List<Object> get props => [title, content, forumId];
}