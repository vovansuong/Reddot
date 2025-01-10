import 'dart:async';

import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:flutterapp/config/theme/theme_manager.dart';
import 'package:flutterapp/features/members/presentation/bloc/member_bloc.dart';
import 'package:provider/provider.dart';

import '../widgets/member_item_widget.dart';

class MemberListScreen extends StatefulWidget {
  const MemberListScreen({super.key});

  @override
  State<MemberListScreen> createState() => _MemberListScreenState();
}

class _MemberListScreenState extends State<MemberListScreen> {
  final TextEditingController _searchController = TextEditingController();

  void _onSearch(value) {
    context.read<MemberBloc>().add(SearchMemberEvent(query: value));
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
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
              child: BlocConsumer<MemberBloc, MemberState>(
                listener: (context, state) {
                  if (state is MemberFailure) {
                    ScaffoldMessenger.of(context).showSnackBar(
                      SnackBar(
                        content: Text(state.message,
                            style: const TextStyle(color: Colors.red)),
                      ),
                    );
                  }
                },
                builder: (context, state) {
                  if (state is MemberLoading) {
                    return const Center(
                      child: CircularProgressIndicator(),
                    );
                  } else if (state is MemberSuccess) {
                    return GridView.builder(
                      gridDelegate:
                          const SliverGridDelegateWithFixedCrossAxisCount(
                        crossAxisCount: 2,
                        crossAxisSpacing: 4.0,
                        mainAxisSpacing: 4.0,
                      ),
                      itemCount: state.members.length,
                      itemBuilder: (context, index) =>
                          MemberItem(user: state.members[index]),
                    );
                  } else if (state is MemberFailure) {
                    return Center(child: Text(state.message));
                  } else {
                    return const Center(child: Text('No data found'));
                  }
                },
              ),
            ),
          ],
        ),
      ),
    );
  }
}