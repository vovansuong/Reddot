import 'dart:io';

import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:flutterapp/config/theme/theme_manager.dart';
import 'package:flutterapp/core/storage/storage.dart';
import 'package:flutterapp/features/auth/presentation/views/login_screen.dart';
import 'package:flutterapp/features/feed/presentation/views/home_screen.dart';
import 'package:flutterapp/features/members/presentation/views/member_list_screen.dart';
import 'package:image_picker/image_picker.dart';
import 'package:provider/provider.dart';

import '../../../auth/presentation/bloc/auth_bloc.dart';
import '../../../profile/presentation/views/profile_screen.dart';

class AppDrawer extends StatefulWidget {
  const AppDrawer({super.key});

  @override
  State<AppDrawer> createState() => _AppDrawerState();
}

class _AppDrawerState extends State<AppDrawer> {
  File? image;
  String username = "Anonymous";

  String? ownerId;
  Future<String> fetchId() async {
    String? ownerId = await Storage().secureStorage.read(key: 'userId');
    return ownerId!;
  }

  @override
  void initState() {
    super.initState();
    fetchId().then((value) {
      setState(() {
        ownerId = value;
      });
    });
  }

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
    return Drawer(
      child: BlocBuilder<AuthBloc, AuthState>(
        builder: (context, state) {
          return ListView(
            children: [
              ListTile(
                leading: const Icon(
                  Icons.person,
                  size: 70,
                ),
                trailing: Consumer<ThemeService>(
                    builder: (context, ThemeService theme, _) {
                  return IconButton(
                      onPressed: () {
                        theme.toggleTheme();
                      },
                      icon: Icon(theme.darkTheme!
                          ? Icons.sunny
                          : CupertinoIcons.moon_stars));
                }),
              ),
              BlocBuilder<AuthBloc, AuthState>(
                builder: (context, state) {
                  return Padding(
                    padding: const EdgeInsets.symmetric(horizontal: 10),
                    child: ListTile(
                      title: Text(ownerId ?? 'Anonymous'),
                      subtitle: InkWell(
                          onTap: () async {
                            String? ownerId = await Storage()
                                .secureStorage
                                .read(key: 'userId');

                            Navigator.of(context).push(
                              MaterialPageRoute(
                                builder: (context) => ProfileScreen(
                                  ownerId: ownerId!,
                                ),
                              ),
                            );
                          },
                          child: Text('Profile $ownerId!')),
                    ),
                  );
                },
              ),
              Padding(
                padding: const EdgeInsets.all(8.0),
                child: Container(
                  height: 0.5,
                  color: Colors.white,
                ),
              ),
              Padding(
                padding: const EdgeInsets.all(8.0),
                child: Column(
                  mainAxisAlignment: MainAxisAlignment.spaceBetween,
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    TextButton(
                      onPressed: () {
                        context.read<AuthBloc>().add(LoggedOut());
                        Navigator.of(context).pushReplacement(
                          MaterialPageRoute(
                            builder: (context) => const LoginScreen(),
                          ),
                        );
                      },
                      child: const Text('Log out'),
                    ),
                  ],
                ),
              )
            ],
          );
        },
      ),
    );
  }
}