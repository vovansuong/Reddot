part of 'group_bloc.dart';

// enum GroupEventStatus { initial, loading, success, failure }

abstract class GroupState extends Equatable {
  const GroupState();
  @override
  List<Object> get props => [];
}

class GroupLoading extends GroupState {}

final class GroupSuccess extends GroupState {
  final List<ForumGroupEntity> groups;

  const GroupSuccess({required this.groups});

  @override
  List<Object> get props => [groups];
}

class GroupFailure extends GroupState {
  final String message;
  const GroupFailure({required this.message});
}