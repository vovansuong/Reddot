import 'dart:io';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:flutterapp/features/posts/presentation/bloc/comments_bloc.dart';
import 'package:flutterapp/features/posts/presentation/views/comments_screen.dart';
import 'package:flutterapp/features/posts/presentation/widgets/input_text_widget.dart';
import 'package:image_picker/image_picker.dart';

class AddComment extends StatefulWidget {
  const AddComment(
      {super.key, required this.discussionId, required this.title});

  final int discussionId;
  final String title;

  @override
  State<AddComment> createState() => _AddCommentState();
}

class _AddCommentState extends State<AddComment> {
  File? image;
  TextEditingController contentController = TextEditingController();

  late int discussionId = widget.discussionId;

  Future pickImage() async {
    try {
      final image = await ImagePicker().pickImage(source: ImageSource.gallery);
      if (image == null) return;

      final imagePathObject = File(image.path);
      setState(() {
        this.image = imagePathObject;
      });
    } on PlatformException catch (err) {
      print('failed');
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Add new post Comment'),
      ),
      body: BlocListener<CommentsBloc, CommentsState>(
        listener: (context, state) {
          if (state is AddCommentLoaded) {
            Navigator.of(context).push(
              MaterialPageRoute(
                builder: (context) => CommentsScreen(
                    discussionId: state.discussionId,
                    discussionTitle: widget.title),
              ),
            );
            ScaffoldMessenger.of(context).showSnackBar(
              const SnackBar(
                content: Text(
                  'Comment added successfully',
                  style: TextStyle(color: Colors.green),
                ),
              ),
            );
          } else if (state is AddCommentFailure) {
            ScaffoldMessenger.of(context).showSnackBar(
              const SnackBar(
                content: Text(
                  'Failed to add Comment',
                  style: TextStyle(color: Colors.red),
                ),
              ),
            );
          }
        },
        child: SingleChildScrollView(
          child: Padding(
            padding: const EdgeInsets.all(8.0),
            child: Column(
              children: [
                buildInputFieldTextArea('Description', contentController),
                ElevatedButton(
                  onPressed: () {
                    // create Comment
                    if (contentController.text.isEmpty) {
                      ScaffoldMessenger.of(context).showSnackBar(
                        const SnackBar(
                          content: Text(
                            'Description cannot be empty',
                            style: TextStyle(color: Colors.red),
                          ),
                        ),
                      );
                    } else {
                      context.read<CommentsBloc>().add(
                            AddCommentEvent(
                              content: contentController.text,
                              discussionId: discussionId,
                            ),
                          );
                      //to home
                      Navigator.of(context).push(
                        MaterialPageRoute(
                          builder: (context) => CommentsScreen(
                              discussionId: discussionId,
                              discussionTitle: 'Discussion $discussionId'),
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
