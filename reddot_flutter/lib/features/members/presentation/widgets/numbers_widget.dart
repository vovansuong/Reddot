import 'package:flutter/material.dart';
import 'package:flutterapp/features/auth/domain/entities/user_entity.dart';
import 'package:flutterapp/features/members/domain/entities/member_entity.dart';

class NumbersWidget extends StatelessWidget {
  const NumbersWidget({super.key, required this.entity});

  final MemberEntity entity;

  @override
  Widget build(BuildContext context) => Row(
        mainAxisAlignment: MainAxisAlignment.center,
        children: <Widget>[
          buildButton(context, '${entity.reputation}', 'Reputation'),
          buildDivider(),
          buildButton(context, '${entity.totalDiscussions}', 'discussions'),
          buildDivider(),
          buildButton(context, '${entity.totalComments}', 'comments'),
        ],
      );

  Widget buildDivider() => const SizedBox(
        height: 14,
        child: VerticalDivider(),
      );

  Widget buildButton(BuildContext context, String value, String text) =>
      MaterialButton(
        padding: const EdgeInsets.symmetric(vertical: 4),
        onPressed: () {},
        materialTapTargetSize: MaterialTapTargetSize.shrinkWrap,
        child: Column(
          mainAxisSize: MainAxisSize.min,
          mainAxisAlignment: MainAxisAlignment.start,
          children: <Widget>[
            Text(
              value,
              style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 4),
            ),
            const SizedBox(height: 2),
            Text(
              text,
              style: const TextStyle(fontWeight: FontWeight.bold),
            ),
          ],
        ),
      );
}