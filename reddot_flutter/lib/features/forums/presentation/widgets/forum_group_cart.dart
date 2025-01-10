import 'package:flutter/material.dart';

class ForumGroupCart extends StatelessWidget {
  const ForumGroupCart({super.key});

  @override
  Widget build(BuildContext context) {
    return const SizedBox(
      child: Padding(
        padding: EdgeInsets.all(1.0),
        child: Column(
          children: [
            Padding(
              padding: EdgeInsets.all(8.0),
              child: Row(
                children: [
                  // ClipOval(
                  //   child: Image.network('src'),
                  // ),
                  Icon(
                    Icons.check,
                    size: 40,
                  ),
                  SizedBox(
                    width: 10,
                  ),
                  Column(
                    children: [
                      Row(
                        children: [
                          Text(
                            'Forum Group',
                            style: TextStyle(
                              fontWeight: FontWeight.bold,
                            ),
                          ),
                        ],
                      )
                    ],
                  )
                ],
              ),
            ),
            Padding(
              padding: EdgeInsets.only(
                left: 13.0,
                bottom: 10,
              ),
              child: Row(
                children: [
                  Text(
                    'Description',
                    style: TextStyle(
                      fontWeight: FontWeight.bold,
                    ),
                  ),
                ],
              ),
            ),
          ],
        ),
      ),
    );
  }
}