import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:flutterapp/features/feed/presentation/widgets/build_datetime.dart';
import 'package:flutterapp/features/forums/domain/entities/discussion_entity.dart';
import 'package:flutterapp/features/posts/presentation/bloc/comments_bloc.dart';
import 'package:flutterapp/features/posts/presentation/views/comments_screen.dart';

class DiscussionItem extends StatelessWidget {
  const DiscussionItem({super.key, required this.discussion});

  final DiscussionAllEntity discussion;

  @override
  Widget build(BuildContext context) {
    int id = discussion.id ?? 1;
    String title = discussion.title ?? "Discussion Title";
    String author = discussion.author?.username ?? "Author Name";
    DateTime createdAt = discussion.createdDate ?? DateTime.now();

    return Container(
      height: 100,
      padding: const EdgeInsets.all(4.0),
      child: Card(
        shadowColor: Colors.white38,
        child: SingleChildScrollView(
          child: Column(
            children: [
              //list of discussion
              ListTile(
                title: Text(title),
                subtitle: buildCreatedAt(author, createdAt),
                onTap: () {
                  // to discussion
                  Navigator.of(context).pushReplacement(
                    MaterialPageRoute(
                      builder: (context) => BlocProvider.value(
                        value: BlocProvider.of<CommentsBloc>(context),
                        child: CommentsScreen(
                            discussionId: id, discussionTitle: title),
                      ),
                    ),
                  );
                },
              ),
            ],
          ),
        ),
      ),
    );
  }
}