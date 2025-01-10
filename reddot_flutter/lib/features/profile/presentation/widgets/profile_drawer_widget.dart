import 'dart:io';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:flutterapp/features/auth/presentation/bloc/auth_bloc.dart';
import 'package:flutterapp/features/profile/presentation/bloc/profile_bloc.dart';
import 'package:image_picker/image_picker.dart';

import '../../../../core/storage/storage.dart';
import '../views/profile_screen.dart';

class ProfileDrawerWidget extends StatefulWidget {
  const ProfileDrawerWidget({super.key});

  @override
  State<ProfileDrawerWidget> createState() => _ProfileDrawerWidgetState();
}

class _ProfileDrawerWidgetState extends State<ProfileDrawerWidget> {
  File? image;
  String username = "Anonymous";

  String? ownerId;
  Future<String> fetchId() async {
    String? ownerId = await Storage().secureStorage.read(key: 'userId');
    return ownerId ?? "Anonymous";
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
                trailing: Column(
                  children: [
                    image != null
                        ? TextButton(
                            onPressed: () {
                              context
                                  .read<AuthBloc>()
                                  .add(ChangeProfPic(image!));
                            },
                            child: const Text('Submit'))
                        : TextButton(
                            onPressed: () => pickImage(),
                            child: const Text('Change Pic')),
                  ],
                ),
              ),
              BlocBuilder<AuthBloc, AuthState>(
                builder: (context, state) {
                  return Padding(
                    padding: const EdgeInsets.symmetric(horizontal: 10),
                    child: ListTile(
                      title: Text(ownerId ?? "Anonymous"),
                      subtitle: Text('username'),
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
                        context.read<ProfileBloc>().add(
                            GetProfileEvent(username: ownerId ?? "Anonymous"));
                      },
                      child: const Text('My Profile'),
                    ),
                    TextButton(
                      onPressed: () {},
                      child: const Text('Settings'),
                    ),
                    TextButton(
                      onPressed: () {},
                      child: const Text('Help'),
                    ),
                    TextButton(
                      onPressed: () {
                        context.read<AuthBloc>().add(LoggedOut());
                        // Navigator.pushNamed(context, '/login');
                        Navigator.pushReplacementNamed(context, '/login');
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