import 'package:bloc/bloc.dart';
import 'package:equatable/equatable.dart';
import 'package:flutterapp/core/usecases/members/search_memeber.dart';
import 'package:flutterapp/features/members/domain/entities/member_entity.dart';
import 'package:flutterapp/features/members/domain/usecases/search_member_usecase.dart';

part 'member_event.dart';
part 'member_state.dart';

class MemberBloc extends Bloc<MemberEvent, MemberState> {
  //domain usecase
  final SearchMemberUseCase _searchMemberUseCase;

  MemberBloc({
    required SearchMemberUseCase searchMemberUseCase,
  })  : _searchMemberUseCase = searchMemberUseCase,
        super(MemberLoading()) {
    on<SearchMemberEvent>((event, emit) async {
      emit(MemberLoading());
      try {
        await _searchMemberUseCase
            .call(ParamsSearch(query: event.query))
            .then((members) {
          members.fold(
            (l) => emit(MemberFailure(
              message: 'No member found with query: ${event.query}',
            )),
            (members) => emit(MemberSuccess(
              members: members,
              search: event.query,
            )),
          );
        });
      } catch (err) {
        print(err);
      }
    });
  }
}