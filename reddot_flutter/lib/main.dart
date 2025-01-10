import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:flutterapp/features/auth/presentation/views/register_screen.dart';
import 'package:flutterapp/features/forums/presentation/bloc/forum_filter/forum_filter_bloc.dart';
import 'package:flutterapp/features/forums/presentation/bloc/froum_bloc/forum_bloc.dart';
import 'package:flutterapp/features/forums/presentation/bloc/group_bloc/group_bloc.dart';
import 'package:flutterapp/features/members/presentation/bloc/member_bloc.dart';
import 'package:flutterapp/features/posts/presentation/bloc/comments_bloc.dart';
import 'package:flutterapp/features/profile/presentation/bloc/profile_bloc.dart';
import 'package:provider/provider.dart';

import 'config/theme/theme_contants.dart';
import 'config/theme/theme_manager.dart';
import 'features/auth/presentation/bloc/auth_bloc.dart';
import 'features/auth/presentation/views/login_screen.dart';
import 'features/feed/presentation/views/main_screen.dart';
import 'features/forums/presentation/bloc/discussion_bloc/discussion_bloc.dart';
import 'injections_container.dart' as dependencyInjection;
import 'injections_container.dart';

void main() {
  dependencyInjection.init();
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return BlocProvider(
      create: (_) => serviceLocator<AuthBloc>()..add(AppStarted()),
      child: MultiBlocProvider(
        providers: [
          BlocProvider(
            create: (_) => serviceLocator<ProfileBloc>()
              ..add(const GetProfileEvent(username: '')),
          ),
          BlocProvider(
            create: (_) =>
                serviceLocator<MemberBloc>()..add(const SearchMemberEvent()),
          ),
          BlocProvider(
            create: (_) => serviceLocator<GroupBloc>()..add(GetGroupsEvent()),
          ),
          BlocProvider(
            create: (_) =>
                serviceLocator<ForumBloc>()..add(const GetAllForumsEvent()),
          ),
          BlocProvider(
            create: (context) => ForumFilterBloc(
              forumBloc: BlocProvider.of<ForumBloc>(context),
            ),
          ),
          BlocProvider(
            create: (_) =>
                serviceLocator<CommentsBloc>()..add(const LoadCommentsEvent()),
          ),
          BlocProvider(
            create: (_) => serviceLocator<DiscussionBloc>()
              ..add(const GetAllDiscussionsEvent()),
          ),
        ],
        child: ChangeNotifierProvider<ThemeService>(
          create: (context) => ThemeService(),
          child: Consumer(
            builder: (context, ThemeService theme, _) {
              return MaterialApp(
                title: 'TechForum',
                debugShowCheckedModeBanner: false,
                theme: theme.darkTheme! ? darkTheme : lightTheme,
                home: BlocConsumer<AuthBloc, AuthState>(
                  listener: (context, state) {},
                  builder: (context, state) {
                    if (state is Authenticated) {
                      return const MainScreen();
                    } else if (state is Unauthenticated) {
                      return const LoginScreen();
                    } else {
                      return Container();
                    }
                  },
                ),
              );
            },
          ),
        ),
      ),
    );
  }
}