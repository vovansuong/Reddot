import 'package:equatable/equatable.dart';
import 'package:flutterapp/core/usecases/search/get_all_discussion.dart';
import 'package:flutterapp/features/forums/domain/entities/discussion_entity.dart';
import 'package:bloc/bloc.dart';
import 'package:flutterapp/features/forums/domain/usecases/get_all_discussion.dart';

part 'discussion_event.dart';
part 'discussion_state.dart';

class DiscussionBloc extends Bloc<GetAllDiscussionsEvent, DiscussionState> {
  final GetAllDiscussionCase _getAllDiscussionCase;
  DiscussionBloc({
    required GetAllDiscussionCase getAllDiscussionCase,
  })  : _getAllDiscussionCase = getAllDiscussionCase,
        super(DiscussionLoading()) {
    on<GetAllDiscussionsEvent>((event, emit) async {
      emit(DiscussionLoading());
      try {
        await _getAllDiscussionCase
            .call(ParamsGetDiscussionTitle(title: event.search))
            .then((discussions) {
          discussions.fold(
            (l) => emit(
                const DiscussionFailure(message: "Error loading discussion")),
            (discussions) => emit(DiscussionSuccess(
              discussions: discussions,
              search: event.search,
            )),
          );
        });
      } catch (err) {
        print(err);
      }
    });
  }
}