import 'dart:async';

import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:flutterapp/features/forums/presentation/bloc/discussion_bloc/discussion_bloc.dart';
import 'package:flutterapp/features/forums/presentation/views/discussion_item_widget.dart';

class ForumScreen extends StatefulWidget {
  const ForumScreen({super.key});

  @override
  State<ForumScreen> createState() => _ForumScreenState();
}

class _ForumScreenState extends State<ForumScreen> {
  final TextEditingController _searchController = TextEditingController();

  void _onSearch(value) {
    context.read<DiscussionBloc>().add(GetAllDiscussionsEvent(search: value));
  }

  @override
  Widget build(BuildContext context) {
    return SafeArea(
      child: Scaffold(
          appBar: AppBar(
            bottom: PreferredSize(
              preferredSize: const Size.fromHeight(40.0),
              child: Padding(
                padding: const EdgeInsets.all(8.0),
                child: TextField(
                  decoration: InputDecoration(
                    hintText: 'Search...',
                    prefixIcon: const Icon(Icons.search),
                    border: OutlineInputBorder(
                      borderRadius: BorderRadius.circular(10.0),
                    ),
                  ),
                  controller: _searchController,
                  onChanged: (value) {
                    //delay search
                    Timer(const Duration(seconds: 1), () => _onSearch(value));
                  },
                ),
              ),
            ),
          ),
          body: SingleChildScrollView(
            physics: BouncingScrollPhysics(),
            child: Column(
              children: [
                SizedBox(
                  height: 700,
                  child: BlocConsumer<DiscussionBloc, DiscussionState>(
                    listener: (context, state) {
                      if (state is DiscussionFailure) {
                        ScaffoldMessenger.of(context).showSnackBar(
                          SnackBar(
                            content: Text(state.message,
                                style: const TextStyle(color: Colors.red)),
                          ),
                        );
                      }
                    },
                    builder: (context, state) {
                      if (state is DiscussionLoading) {
                        return const Center(
                          child: CircularProgressIndicator(),
                        );
                      } else if (state is DiscussionSuccess) {
                        return ListView.builder(
                          itemCount: state.discussions.length,
                          itemBuilder: (context, index) => DiscussionItem(
                              discussion: state.discussions[index]),
                        );
                      } else if (state is DiscussionFailure) {
                        return Center(child: Text(state.message));
                      } else {
                        return const Center(child: Text('No data found'));
                      }
                    },
                  ),
                ),
              ],
            ),
          )),
    );
  }
}