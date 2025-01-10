import 'package:flutter/material.dart';
import 'package:flutterapp/features/profile/domain/entities/user_pro_entity.dart';

class NumbersWidget extends StatelessWidget {
  const NumbersWidget({super.key, required this.user});

  final UserProEntity user;

  @override
  Widget build(BuildContext context) => Row(
        mainAxisAlignment: MainAxisAlignment.center,
        children: <Widget>[
          buildButton(context, '${user.reputation}', 'Reputation'),
          buildDivider(),
          buildButton(context, '${user.totalFollowers}', 'Followers'),
          buildDivider(),
          buildButton(context, '${user.totalFollowing}', 'Following'),
        ],
      );
  Widget buildDivider() => const SizedBox(
        height: 24,
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
              style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 24),
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