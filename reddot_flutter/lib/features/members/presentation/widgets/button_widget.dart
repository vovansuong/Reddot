import 'package:flutter/material.dart';

class ButtonWidget extends StatelessWidget {
  final String text;
  final VoidCallback onClicked;

  const ButtonWidget({
    super.key,
    required this.text,
    required this.onClicked,
  });

  @override
  Widget build(BuildContext context) => Container(
        width: double.infinity,
        padding: const EdgeInsets.symmetric(horizontal: 10, vertical: 0),
        child: ElevatedButton(
          style: ElevatedButton.styleFrom(
            foregroundColor: Colors.white,
            backgroundColor: Colors.blue,
            shape: const StadiumBorder(),
            padding: const EdgeInsets.symmetric(horizontal: 10, vertical: 7),
          ),
          onPressed: onClicked,
          child: Text(text),
        ),
      );
}