import 'dart:async';

import 'package:bloc/bloc.dart';
import 'package:equatable/equatable.dart';
import 'package:flutterapp/features/forums/domain/entities/forum_entity.dart';
import 'package:flutterapp/features/forums/presentation/bloc/froum_bloc/forum_bloc.dart';

part 'forum_filter_event.dart';
part 'forum_filter_state.dart';

class ForumFilterBloc extends Bloc<ForumFilterEvent, ForumFilterState> {
  final ForumBloc _forumBloc;
  late StreamSubscription _forumSubscription;

  ForumFilterBloc({required ForumBloc forumBloc})
      : _forumBloc = forumBloc,
        super(ForumFilterLoading()) {
    on<UpdateFilter>(_onUpdateFilter);
    on<UpdateForums>(_onUpdateForums);
    _forumSubscription = forumBloc.stream.listen((state) {
      add(const UpdateFilter());
    });
  }

  void _onUpdateFilter(UpdateFilter event, Emitter<ForumFilterState> emit) {
    if (state is ForumFilterLoading) {
      add(const UpdateForums(
        forumFilter: -1,
      ));
    }

    if (state is ForumFilterLoaded) {
      final state = this.state as ForumFilterLoaded;
      add(UpdateForums(forumFilter: state.filter));
    }
  }

  void _onUpdateForums(
      UpdateForums event, Emitter<ForumFilterState> emit) async {
    final state = _forumBloc.state;
    if (state is ForumSuccess) {
      final List<ForumEntity> forums = state.forums.where((forum) {
        if (event.forumFilter == -1) {
          return true;
        } else {
          return forum.groupId! == event.forumFilter;
        }
      }).toList();
      emit(ForumFilterLoaded(forums: forums, filter: event.forumFilter));
    }
  }
}