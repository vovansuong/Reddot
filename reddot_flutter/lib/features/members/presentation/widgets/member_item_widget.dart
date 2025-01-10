import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:flutterapp/core/network/api_urls.dart';
import 'package:flutterapp/features/members/domain/entities/member_entity.dart';
import 'package:flutterapp/features/members/presentation/widgets/button_widget.dart';
import 'package:flutterapp/features/members/presentation/widgets/numbers_widget.dart';
import 'package:flutterapp/features/members/presentation/widgets/profile_widget.dart';
import 'package:flutterapp/features/profile/presentation/bloc/profile_bloc.dart';
import 'package:flutterapp/features/profile/presentation/views/profile_screen.dart';

class MemberItem extends StatelessWidget {
  const MemberItem({super.key, required this.user});

  final MemberEntity user;

  @override
  Widget build(BuildContext context) {
    final avatar = user.imageUrl != ""
        ? user.imageUrl
        : user.avatar != ""
            ? '${ApiUrls.API_BASE_URL}/user-stat/images/${user.avatar}'
            : "https://lh3.googleusercontent.com/a/ACg8ocIKA_Jkp2pWe0wuRjRJvAGJ0_tdjLSK2iBDmIVGTjRAe6B6EJDW=s96-c";
    return Container(
      height: 500,
      padding: const EdgeInsets.all(4.0),
      child: Card(
        shadowColor: Colors.white38,
        child: SingleChildScrollView(
          child: Column(
            children: [
              const SizedBox(height: 10),
              SizedBox(
                height: 100,
                child: ProfileWidget(
                  imagePath: avatar,
                  width: 100,
                  height: 100,
                ),
              ),
              SizedBox(
                height: 10,
              ),
              InkWell(
                  onTap: () {
                    context
                        .read<ProfileBloc>()
                        .add(GetProfileEvent(username: user.username));
                    //route to profile screen
                    Navigator.push(
                      context,
                      MaterialPageRoute(
                        builder: (context) => ProfileScreen(
                          ownerId: user.username,
                        ),
                      ),
                    );
                  },
                  child: buildName(user)),
              Center(
                child: TextButton(
                  onPressed: () {},
                  child: Text(
                    user.status,
                    style: const TextStyle(
                      fontSize: 12,
                    ),
                  ),
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }

  //----------------------------------------------------------------
  Widget buildName(MemberEntity user) {
    final name = user.name != "" ? user.name : user.username;
    return Column(
      children: [
        Text(
          name,
          style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 14),
        ),
        const SizedBox(height: 2),
      ],
    );
  }

  Widget buildFollowButton() => ButtonWidget(
        text: 'Follow',
        onClicked: () {},
      );
  Widget buildChatButton() => ButtonWidget(
        text: 'Chat',
        onClicked: () {},
      );
}