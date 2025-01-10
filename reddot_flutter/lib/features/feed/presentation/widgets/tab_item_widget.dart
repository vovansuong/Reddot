import 'package:flutter/material.dart';

class TabItem extends StatelessWidget {
  const TabItem({super.key, required this.title, required this.color});

  final String title;
  final String color;

  @override
  Widget build(BuildContext context) {
    return Tab(
      text: title,
      icon: Icon(
        Icons.signpost,
        color: Colors.indigo.shade500,
      ),
    );
  }
}