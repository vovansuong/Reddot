part of 'forum_filter_bloc.dart';

enum ForumFilter { all, g1, g2, g3, g4 }

abstract class ForumFilterState extends Equatable {
  const ForumFilterState();

  @override
  List<Object> get props => [];
}

class ForumFilterLoading extends ForumFilterState {}

class ForumFilterLoaded extends ForumFilterState {
  final List<ForumEntity> forums;
  final int filter;

  const ForumFilterLoaded({
    required this.forums,
    this.filter = -1,
  });

  @override
  List<Object> get props => [forums, filter];
}