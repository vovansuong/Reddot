import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:flutterapp/features/feed/presentation/widgets/app_drawer_widget.dart';
import 'package:flutterapp/features/feed/presentation/widgets/avatar_widget.dart';
import 'package:flutterapp/features/feed/presentation/widgets/build_datetime.dart';
import 'package:flutterapp/features/feed/presentation/widgets/tab_item_widget.dart';
import 'package:flutterapp/features/forums/domain/entities/forum_entity.dart';
import 'package:flutterapp/features/forums/domain/entities/forum_group_entity.dart';
import 'package:flutterapp/features/forums/presentation/bloc/forum_filter/forum_filter_bloc.dart';
import 'package:flutterapp/features/forums/presentation/bloc/froum_bloc/forum_bloc.dart';
import 'package:flutterapp/features/forums/presentation/bloc/group_bloc/group_bloc.dart';
import 'package:flutterapp/features/posts/presentation/bloc/comments_bloc.dart';
import 'package:flutterapp/features/posts/presentation/views/comments_screen.dart';
import 'package:flutterapp/features/posts/presentation/views/create_discussion.dart';
import 'package:intl/intl.dart';
import 'package:provider/provider.dart';
import '../../../../config/theme/theme_manager.dart';

class HomeScreen extends StatefulWidget {
  const HomeScreen({super.key});

  @override
  State<HomeScreen> createState() => _HomeScreenState();
}

class _HomeScreenState extends State<HomeScreen>
    with SingleTickerProviderStateMixin {
  @override
  Widget build(BuildContext context) {
    return SafeArea(
      child: Scaffold(
        drawer: const AppDrawer(),
        body: BlocBuilder<GroupBloc, GroupState>(builder: (context, state) {
          if (state is GroupLoading) {
            return const Center(
              child: CircularProgressIndicator(),
            );
          } else if (state is GroupSuccess) {
            return DefaultTabController(
              length: state.groups.length + 1,
              child: NestedScrollView(
                headerSliverBuilder: (context, innerBoxIsScrolled) {
                  return [
                    SliverAppBar(
                      title: const Text("Tech Forums"),
                      expandedHeight: 30.0,
                      pinned: true,
                      floating: false,
                      forceElevated: false,
                      bottom: TabBar(
                        onTap: (tabIndex) {
                          if (tabIndex == 0) {
                            BlocProvider.of<ForumFilterBloc>(context).add(
                              const UpdateForums(
                                forumFilter: -1,
                              ),
                            );
                          } else {
                            BlocProvider.of<ForumFilterBloc>(context).add(
                              UpdateForums(
                                forumFilter: state.groups[tabIndex - 1].id,
                              ),
                            );
                          }
                        },
                        isScrollable: true, // Cho phép cuộn ngang
                        tabs: [
                          const TabItem(title: "All", color: "blue"),
                          ...state.groups.map((tab) =>
                              TabItem(title: tab.title, color: tab.color))
                        ],
                      ),
                    ),
                  ];
                },
                body: TabBarView(
                  children: [
                    _toForumGroup("All"),
                    ...state.groups.map((tab) => _toForumGroup(tab.title)),
                  ],
                ),
              ),
            );
          } else {
            return const Center(
              child: Text('Something went wrong'),
            );
          }
        }),
      ),
    );
  }

  //----------------------------------------
  BlocConsumer<ForumFilterBloc, ForumFilterState> _toForumGroup(String title) {
    return BlocConsumer<ForumFilterBloc, ForumFilterState>(
      listener: (context, state) {
        context.read<ForumBloc>().add(GetAllForumsEvent());
      },
      builder: (context, state) {
        if (state is ForumFilterLoading) {
          return const Center(
            child: CircularProgressIndicator(),
          );
        } else if (state is ForumFilterLoaded && state.forums.isEmpty) {
          return const Center(
            child: Text('No forums found'),
          );
        } else if (state is ForumFilterLoaded) {
          return Padding(
            padding: const EdgeInsets.all(16.0),
            child: ListView.builder(
                shrinkWrap: true,
                itemCount: state.forums.length,
                itemBuilder: (context, index) {
                  return _forumItemCard(context, state.forums[index]);
                }),
          );
        } else {
          return const Text('Something went wrong');
        }
      },
    );
  }

  Container _forumItemCard(BuildContext context, ForumEntity forum) {
    if (forum.discussions.isEmpty) {
      return Container(
        margin: const EdgeInsets.only(bottom: 8.0),
        decoration: const BoxDecoration(
          border: Border(
            bottom: BorderSide(
              color: Colors.grey,
              width: 1,
            ),
          ),
        ),
        child: Padding(
          padding: const EdgeInsets.all(8.0),
          child: Column(
            mainAxisAlignment: MainAxisAlignment.spaceBetween,
            children: <Widget>[
              Row(
                children: [
                  Text(
                    '${(forum.title)?.toUpperCase()}',
                    style: const TextStyle(
                      fontSize: 18.0,
                      fontWeight: FontWeight.bold,
                    ),
                  ),
                  IconButton(
                    onPressed: () {
                      Navigator.of(context).push(
                        MaterialPageRoute(
                          builder: (context) => CreateDiscussion(
                            forumId: forum.id ?? 1,
                            title: forum.title ?? 'Discussion',
                          ),
                        ),
                      );
                    },
                    icon: const Icon(Icons.add_circle_sharp),
                  ),
                ],
              ),
              const SizedBox(
                height: 4,
              ),
              const Text('You are the first to post a discussion.'),
            ],
          ),
        ),
      );
    }
    return Container(
      margin: const EdgeInsets.only(bottom: 8.0),
      decoration: const BoxDecoration(
        border: Border(
          bottom: BorderSide(
            color: Colors.grey,
            width: 1,
          ),
        ),
      ),
      child: Column(
        mainAxisAlignment: MainAxisAlignment.spaceBetween,
        crossAxisAlignment: CrossAxisAlignment.start,
        children: <Widget>[
          Row(
            mainAxisSize: MainAxisSize.min,
            children: [
              Text(
                '${(forum.title)?.toUpperCase()}',
                style: const TextStyle(
                  fontSize: 18.0,
                  fontWeight: FontWeight.bold,
                ),
              ),
              IconButton(
                onPressed: () {
                  Navigator.of(context).push(
                    MaterialPageRoute(
                      builder: (context) => CreateDiscussion(
                        forumId: forum.id ?? 1,
                        title: forum.title ?? 'Discussion',
                      ),
                    ),
                  );
                },
                icon: const Icon(Icons.add_circle_sharp),
              ),
            ],
          ),
          //list of discussions
          ListView.builder(
              shrinkWrap: true,
              itemCount: forum.discussions.length,
              itemBuilder: (context, index) {
                return _discussionItemCard(
                    context, forum.discussions[index], forum.id);
              }),
        ],
      ),
    );
  }

  Container _discussionItemCard(
      BuildContext context, DiscussionEntity discussion, int? forumId) {
    int discussionId = discussion.discussionId ?? 1;
    String title = discussion.discussionTitle ?? 'Discussion';
    String author = discussion.name ?? discussion.username ?? 'Anonymous';
    DateTime createdAt = discussion.createdAt ?? DateTime.now();

    return Container(
      margin: const EdgeInsets.only(bottom: 4.0),
      child: Card(
        child: SingleChildScrollView(
          child: Row(
            mainAxisSize: MainAxisSize.min,
            children: [
              Padding(
                padding: const EdgeInsets.all(4.0),
                child: _buildImage(discussion),
              ),
              Flexible(
                child: Column(
                  mainAxisAlignment: MainAxisAlignment.spaceBetween,
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: <Widget>[
                    Row(
                      mainAxisSize: MainAxisSize.min,
                      children: [
                        Flexible(
                          child: TextButton(
                            onPressed: () {
                              context
                                  .read<CommentsBloc>()
                                  .add(LoadCommentsEvent(
                                    discussionId: discussionId,
                                  ));
                              Navigator.of(context).push(
                                MaterialPageRoute(
                                  builder: (context) => CommentsScreen(
                                    discussionId: discussionId,
                                    discussionTitle: title,
                                  ),
                                ),
                              );
                            },
                            child: Text(
                              '#$title',
                              style: const TextStyle(
                                fontSize: 18.0,
                                fontWeight: FontWeight.bold,
                              ),
                            ),
                          ),
                        ),
                      ],
                    ),
                    const SizedBox(
                      height: 4,
                    ),
                    buildCreatedAt(author, createdAt),
                    const SizedBox(
                      height: 8,
                    ),
                    //list of discussions
                  ],
                ),
              ),
              Container(
                height: 1,
                color: Colors.grey,
                margin: const EdgeInsets.symmetric(vertical: 8),
              ),
            ],
          ),
        ),
      ),
    );
  }

  Widget _buildImage(DiscussionEntity discussion) {
    return buildAvatar(
      imageUrl: discussion.imageUrl ?? '',
      avatar: discussion.avatar ?? '',
      width: 42,
      height: 42,
    );
  }
}