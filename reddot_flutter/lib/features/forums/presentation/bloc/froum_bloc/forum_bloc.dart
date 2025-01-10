import 'package:bloc/bloc.dart';
import 'package:equatable/equatable.dart';
import 'package:flutterapp/core/usecases/forums/get_all_forum.dart';
import 'package:flutterapp/features/forums/domain/entities/forum_entity.dart';
import 'package:flutterapp/features/forums/domain/usecases/get_all_forum.dart';

part 'forum_event.dart';
part 'forum_state.dart';

class ForumBloc extends Bloc<ForumEvent, ForumState> {
  //domain usecase
  final GetAllForumUseCase _getAllForumUseCase;
  ForumBloc({
    required GetAllForumUseCase getAllForumUseCase,
  })  : _getAllForumUseCase = getAllForumUseCase,
        super(ForumLoading()) {
    on<GetAllForumsEvent>((event, emit) async {
      emit(ForumLoading());
      try {
        await _getAllForumUseCase.call(NoParams()).then((forums) {
          forums.fold(
            (l) => emit(const ForumFailure(message: "Error loading forums")),
            (forum) => emit(ForumSuccess(forums: forum)),
          );
        });
      } catch (err) {
        print(err);
      }
    });
  }
}