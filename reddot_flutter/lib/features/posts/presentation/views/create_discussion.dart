import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:flutterapp/features/forums/presentation/bloc/forum_filter/forum_filter_bloc.dart';
import 'package:flutterapp/features/forums/presentation/bloc/froum_bloc/forum_bloc.dart';
import 'package:flutterapp/features/posts/presentation/bloc/comments_bloc.dart';
import 'package:flutterapp/features/posts/presentation/views/comments_screen.dart';
import 'package:flutterapp/features/posts/presentation/widgets/input_text_widget.dart';
import 'package:flutterapp/features/posts/presentation/widgets/input_widget.dart';

class CreateDiscussion extends StatelessWidget {
  const CreateDiscussion({
    super.key,
    required this.forumId,
    required this.title,
  });

  final int forumId;
  final String title;

  @override
  Widget build(BuildContext context) {
    TextEditingController titleController = TextEditingController();
    TextEditingController contentController = TextEditingController();

    return Scaffold(
      appBar: AppBar(
        title: Text('Add new post $title'),
        leading: IconButton(
          icon: const Icon(Icons.arrow_back),
          onPressed: () {
            // to home
            context.read<ForumBloc>().add(const GetAllForumsEvent());
            context.read<ForumFilterBloc>().add(const UpdateForums());
            Navigator.of(context).pop();
          },
        ),
      ),
      body: BlocListener<CommentsBloc, CommentsState>(
        listener: (context, state) {
          if (state is CreateDiscussionLoaded) {
            Navigator.of(context).pushReplacement(
              MaterialPageRoute(
                builder: (context) => BlocProvider.value(
                  value: BlocProvider.of<CommentsBloc>(context),
                  child: CommentsScreen(
                      discussionId: state.discussionId,
                      discussionTitle: titleController.text),
                ),
              ),
            );
            ScaffoldMessenger.of(context).showSnackBar(
              const SnackBar(
                content: Text(
                  'Discussion added successfully',
                  style: TextStyle(color: Colors.green),
                ),
              ),
            );
          } else if (state is CreateDiscussionFailure) {
            ScaffoldMessenger.of(context).showSnackBar(
              const SnackBar(
                content: Text(
                  'Failed to add discussion',
                  style: TextStyle(color: Colors.red),
                ),
              ),
            );
          }
        },
        child: SingleChildScrollView(
          child: Padding(
            padding: const EdgeInsets.all(10.0),
            child: Column(
              children: [
                buildInputField('Title', titleController),
                buildInputFieldTextArea('Description', contentController),
                ElevatedButton(
                  onPressed: () {
                    if (titleController.text.isEmpty ||
                        contentController.text.isEmpty) {
                      ScaffoldMessenger.of(context).showSnackBar(
                        const SnackBar(
                          content: Text(
                            'Title and Description cannot be empty',
                            style: TextStyle(color: Colors.red),
                          ),
                        ),
                      );
                    } else {
                      context.read<CommentsBloc>().add(
                            AddDiscussionEvent(
                              title: titleController.text,
                              content: contentController.text,
                              forumId: forumId,
                            ),
                          );
                    }
                  },
                  style: ElevatedButton.styleFrom(
                    backgroundColor: Theme.of(context).primaryColor,
                  ),
                  child: const Text(
                    'Add new',
                    style: TextStyle(color: Colors.white),
                  ),
                ),
              ],
            ),
          ),
        ),
      ),
    );
  }
}
