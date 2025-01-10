import 'package:bloc/bloc.dart';
import 'package:equatable/equatable.dart';
import 'package:flutterapp/core/usecases/forums/get_group_core.dart';
import 'package:flutterapp/features/forums/domain/entities/forum_group_entity.dart';
import 'package:flutterapp/features/forums/domain/usecases/get_all_group.dart';

part 'group_event.dart';
part 'group_state.dart';

class GroupBloc extends Bloc<GroupEvent, GroupState> {
  //domain
  final GetAllGroupsUseCase _getAllGroups;

  GroupBloc({
    required GetAllGroupsUseCase getAllGroupsUseCase,
  })  : _getAllGroups = getAllGroupsUseCase,
        super(GroupLoading()) {
    on<GetGroupsEvent>((event, emit) async {
      emit(GroupLoading());
      try {
        await _getAllGroups.call(NoParams()).then((groups) {
          groups.fold(
            (l) => emit(const GroupFailure(message: "Error loading groups")),
            (group) {
              emit(GroupSuccess(groups: group));
            },
          );
        });
      } catch (err) {
        print(err);
      }
    });
  }
}