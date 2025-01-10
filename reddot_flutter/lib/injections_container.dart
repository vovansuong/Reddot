import 'package:flutterapp/core/network/session.dart';
import 'package:flutterapp/features/forums/data/data_sources/forum_data_source.dart';
import 'package:flutterapp/features/forums/domain/repository/forum_repo.dart';
import 'package:flutterapp/features/forums/domain/usecases/get_all_group.dart';
import 'package:flutterapp/features/forums/presentation/bloc/froum_bloc/forum_bloc.dart';
import 'package:flutterapp/features/forums/presentation/bloc/group_bloc/group_bloc.dart';
import 'package:flutterapp/features/members/data/data_sources/member_data_source.dart';
import 'package:flutterapp/features/posts/data/repository/discussion_repo_impl.dart';
import 'package:flutterapp/features/posts/domain/repository/discussion_repo.dart';
import 'package:flutterapp/features/posts/presentation/bloc/comments_bloc.dart';
import 'package:flutterapp/features/profile/domain/usecases/get_user_pro.dart';
import 'package:get_it/get_it.dart';
import 'package:http/http.dart' as http;

import 'features/auth/data/data_sources/auth_data_source.dart';
import 'features/auth/data/repository/auth_repo_impl.dart';
import 'features/auth/domain/repository/auth_repo.dart';
import 'features/auth/domain/usecases/change_pro_img.dart';
import 'features/auth/domain/usecases/login_user.dart';
import 'features/auth/domain/usecases/register_user.dart';
import 'features/auth/presentation/bloc/auth_bloc.dart';
import 'features/forums/data/repository/forum_repo_impl.dart';
import 'features/forums/domain/usecases/get_all_discussion.dart';
import 'features/forums/domain/usecases/get_all_forum.dart';
import 'features/forums/presentation/bloc/discussion_bloc/discussion_bloc.dart';
import 'features/members/data/repository/member_repo_impl.dart';
import 'features/members/domain/repository/member_repo.dart';
import 'features/members/domain/usecases/get_all_memeber.dart';
import 'features/members/domain/usecases/search_member_usecase.dart';
import 'features/members/presentation/bloc/member_bloc.dart';
import 'features/posts/data/data_sources/discussion_data_source.dart';
import 'features/posts/domain/usecases/create_comment.dart';
import 'features/posts/domain/usecases/create_discussion.dart';
import 'features/posts/domain/usecases/get_comments_by.dart';
import 'features/profile/data/data_sources/profile_data_source.dart';
import 'features/profile/data/repository/profile_repo_impl.dart';
import 'features/profile/domain/repository/profile_repo.dart';
import 'features/profile/domain/usecases/edit_info_usecase.dart';
import 'features/profile/presentation/bloc/profile_bloc.dart';

final serviceLocator = GetIt.instance;

void init() {
  //----------------------------features
  //bloc
  serviceLocator.registerFactory(
    () => AuthBloc(
        loginUserUsecase: serviceLocator(),
        registerUser: serviceLocator(),
        changeProImg: serviceLocator()),
  );

  serviceLocator.registerFactory(
    () => ProfileBloc(
      getUserProUseCase: serviceLocator(),
      updateInfoUseCase: serviceLocator(),
    ),
  );

  serviceLocator.registerFactory(
    () => MemberBloc(
      searchMemberUseCase: serviceLocator(),
    ),
  );

  serviceLocator.registerFactory(
    () => GroupBloc(
      getAllGroupsUseCase: serviceLocator(),
    ),
  );

  serviceLocator.registerFactory(
    () => ForumBloc(
      getAllForumUseCase: serviceLocator(),
    ),
  );

  serviceLocator.registerFactory(
    () => CommentsBloc(
      getAllCommentsUseCase: serviceLocator(),
      createDiscussionUseCase: serviceLocator(),
      createCommentUseCase: serviceLocator(),
      forumBloc: serviceLocator(),
      getAllForumUseCase: serviceLocator(),
    ),
  );

  serviceLocator.registerFactory(
    () => DiscussionBloc(
      getAllDiscussionCase: serviceLocator(),
    ),
  );

  //----------------------------features
  //useCases
  serviceLocator
      .registerLazySingleton(() => LoginUser(repository: serviceLocator()));

  serviceLocator
      .registerLazySingleton(() => RegisterUser(repository: serviceLocator()));

  serviceLocator
      .registerLazySingleton(() => ChangeProImg(repository: serviceLocator()));

  serviceLocator.registerLazySingleton(
      () => GetUserProUseCase(repository: serviceLocator()));

  // serviceLocator.registerLazySingleton(
  //     () => GetAllMemberUseCase(repository: serviceLocator()));

  serviceLocator.registerLazySingleton(
      () => SearchMemberUseCase(repository: serviceLocator()));

  serviceLocator.registerLazySingleton(
      () => UpdateInfoUseCase(repository: serviceLocator()));

  serviceLocator.registerLazySingleton(
      () => GetAllGroupsUseCase(repository: serviceLocator()));

  serviceLocator.registerLazySingleton(
      () => GetAllForumUseCase(repository: serviceLocator()));

  serviceLocator.registerLazySingleton(
      () => GetAllCommentsUseCase(repository: serviceLocator()));

  serviceLocator.registerLazySingleton(
      () => CreateDiscussionUseCase(repository: serviceLocator()));

  serviceLocator.registerLazySingleton(
      () => CreateCommentUseCase(repository: serviceLocator()));

  serviceLocator.registerLazySingleton(
      () => GetAllDiscussionCase(repository: serviceLocator()));

  //----------------------------features
  //Repository
  serviceLocator.registerLazySingleton<AuthRepo>(
      () => AuthRepoImpl(authDataSource: serviceLocator()));

  serviceLocator.registerLazySingleton<ProfileRepo>(
      () => ProfileRepoImpl(profileDataSource: serviceLocator()));

  serviceLocator.registerLazySingleton<MemberRepo>(
      () => MemberRepoImpl(memberDataSource: serviceLocator()));

  serviceLocator.registerLazySingleton<ForumRepo>(
      () => ForumRepoImpl(forumDataSource: serviceLocator()));

  serviceLocator.registerLazySingleton<DiscussionRepo>(
      () => DiscussionRepoImpl(discussionDataSource: serviceLocator()));

  //data sources

  serviceLocator.registerLazySingleton<AuthDataSource>(
      () => AuthDataSourceImp(client: serviceLocator()));

  serviceLocator.registerLazySingleton<ProfileDataSource>(
      () => ProfileDataSourceImpl(client: serviceLocator()));

  serviceLocator.registerLazySingleton<MemberDataSource>(
      () => MemberDataSourceImp(client: serviceLocator()));

  serviceLocator.registerLazySingleton<ForumDataSource>(
      () => ForumDataSourceImp(client: serviceLocator()));

  serviceLocator.registerLazySingleton<DiscussionDataSource>(
      () => DiscussionDataSourceImpl(client: serviceLocator()));

  //----------------------------core

  serviceLocator.registerLazySingleton(() => NetworkService());

  //---------------------------- external dependencies
  serviceLocator.registerLazySingleton(() => http.Client());
}