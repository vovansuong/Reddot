import 'package:flutter/material.dart';

class HomeScreen extends StatefulWidget {
  const HomeScreen({super.key});

  @override
  State<HomeScreen> createState() => _HomeScreenState();
}

class _HomeScreenState extends State<HomeScreen> {
  final List<Map<String, dynamic>> _forums = [
    {
      'title': 'General',
      'subForums': [
        {'title': 'Announcements', 'posts': 100, 'users': 50},
        {'title': 'Feedback & Suggestions', 'posts': 75, 'users': 30},
        {'title': 'Rules & Guidelines', 'posts': 50, 'users': 20},
      ],
    },
    {
      'title': 'Tech',
      'subForums': [
        {'title': 'Programming', 'posts': 250, 'users': 100},
        {'title': 'Hardware', 'posts': 150, 'users': 75},
        {'title': 'Software', 'posts': 200, 'users': 90},
      ],
    },
    {
      'title': 'Hobbies',
      'subForums': [
        {'title': 'Sports', 'posts': 180, 'users': 80},
        {'title': 'Arts & Crafts', 'posts': 120, 'users': 60},
        {'title': 'Gaming', 'posts': 220, 'users': 100},
      ],
    },
    {
      'title': 'Lifestyle',
      'subForums': [
        {'title': 'Health & Fitness', 'posts': 150, 'users': 70},
        {'title': 'Travel', 'posts': 180, 'users': 80},
        {'title': 'Food & Recipes', 'posts': 200, 'users': 90},
      ],
    },
  ];

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Home Forum'),
      ),
      body: RefreshIndicator(
        onRefresh: _refreshForums,
        child: ListView.builder(
          padding: const EdgeInsets.all(16.0),
          itemCount: _forums.length,
          itemBuilder: (context, index) {
            final forum = _forums[index];
            return ExpansionTile(
              title: Text(
                forum['title'],
                style: const TextStyle(
                  fontSize: 20.0,
                  color:
                      Color.fromARGB(255, 127, 162, 197), // Set the title color
                ),
              ),
              children: [
                for (final subForum in forum['subForums'])
                  ListTile(
                    onTap: () {
                      // Navigate to the specific sub-forum page
                      Navigator.push(
                        context,
                        MaterialPageRoute(
                          builder: (context) => ForumPage(subForum['title']),
                        ),
                      );
                    },
                    title: Text(
                      subForum['title'],
                      style: const TextStyle(
                        color: Color.fromARGB(255, 209, 196,
                            196), // Set the title color for sub-forums
                      ),
                    ),
                    subtitle: Column(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        Text(
                          'Posts: ${subForum['posts']}',
                          style: const TextStyle(
                            color: Color.fromARGB(255, 185, 179,
                                179), // Set the color for "Posts:" text
                          ),
                        ),
                        Text(
                          'Users: ${subForum['users']}',
                          style: const TextStyle(
                            color: Color.fromARGB(158, 184, 178,
                                178), // Set the color for "Users:" text
                          ),
                        ),
                      ],
                    ),
                  )
              ],
            );
          },
        ),
      ),
    );
  }

  Future<void> _refreshForums() async {
    // Simulating a delay for demonstration purposes
    await Future.delayed(const Duration(seconds: 2));
    setState(() {
      // You can update the forum list here, e.g., fetch new data from an API
    });
  }
}

class ForumPage extends StatelessWidget {
  final String _forumTitle;

  const ForumPage(this._forumTitle, {super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(_forumTitle),
      ),
      body: Center(
        child: Text('This is the $_forumTitle forum page.'),
      ),
    );
  }
}
