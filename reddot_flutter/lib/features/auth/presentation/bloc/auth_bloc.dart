import 'dart:ffi';
import 'dart:io';

import 'package:bloc/bloc.dart';
import 'package:equatable/equatable.dart';
import 'package:flutterapp/core/storage/storage.dart';
import 'package:flutterapp/core/usecases/register_user_core.dart';

import '../../../../core/usecases/change_img_prof.dart';
import '../../../../core/usecases/login_user_core.dart';
import '../../domain/entities/user_entity.dart';
import '../../domain/usecases/change_pro_img.dart';
import '../../domain/usecases/login_user.dart';
import '../../domain/usecases/register_user.dart';

part 'auth_event.dart';
part 'auth_state.dart';

class AuthBloc extends Bloc<AuthEvent, AuthState> {
  //domain usecase
  final LoginUser _loginUserUsecase;
  final RegisterUser _registerUser;
  final ChangeProImg _changeProImg;

  AuthBloc({
    required RegisterUser registerUser,
    required LoginUser loginUserUsecase,
    required ChangeProImg changeProImg,
  })  : _loginUserUsecase = loginUserUsecase,
        _registerUser = registerUser,
        _changeProImg = changeProImg,
        super(Unauthenticated()) {
    on<AppStarted>((event, emit) async {
      var token = await _getToken();
      if (token != "") {
        emit(Authenticated());
      } else {
        emit(Unauthenticated());
      }
    });
    on<LoggedIn>((event, emit) async {
      emit(LoginLoading());
      final email = event.email;
      final password = event.password;
      try {
        final token = await _loginUserUsecase.call(
          Params(
            email: email,
            password: password,
          ),
        );
        token.fold(
            (l) => emit(LoginFailure(message: "Username or password invalid.")),
            (tk) {
          _saveToken(tk);
          emit(Authenticated());
        });
      } catch (e) {
        emit(LoginFailure(message: e.toString()));
      }
    });
    on<LoggedOut>((event, emit) async {
      Storage().token = '';
      Storage().userId = '';
      await _deleteToken();
      await _deleteUserId();
      await _deleteCookies();
      emit(Unauthenticated());
    });
    on<Register>((event, emit) async {
      final username = event.username;
      final email = event.email;
      final password = event.password;

      final res = await _registerUser.call(
        ParamsRegister(
          email: email,
          password: password,
          username: username,
        ),
      );
      res.fold(
          (l) => emit(
              RegisterFailure(message: "Username or email already exists.")),
          (res) => emit(RegisterSuccess()));
    });
    on<ChangeProfPic>((event, emit) {
      emit(ProfPicLoading());
      final filePath = event.file;
      _changeProImg.call(
        ParamsChangeProImg(profileImage: filePath),
      );
      emit(ProfPicSuccess());
    });
  }

  Future<String?> _getToken() async {
    return await Storage().secureStorage.read(key: 'token') ?? '';
  }

  Future<void> _deleteUserId() async {
    await Storage().secureStorage.delete(key: 'userId');
  }

  Future<void> _deleteToken() async {
    await Storage().secureStorage.delete(key: 'token');
  }

  Future<void> _deleteCookies() async {
    await Storage().secureStorage.delete(key: 'cookies');
  }

  Future<void> _saveToken(String token) async {
    await Storage().secureStorage.write(key: 'token', value: token);
  }
}