import 'package:flutter/material.dart';
import 'package:flutterapp/features/profile/data/models/user_pre_model.dart';

class EditProfileScreens extends StatefulWidget {
  final User user;

  const EditProfileScreens({super.key, required this.user});

  @override
  _EditProfileScreenState createState() => _EditProfileScreenState();
}

class _EditProfileScreenState extends State<EditProfileScreens> {
  late TextEditingController _nameController;
  late TextEditingController _emailController;
  late TextEditingController _birthdayController;
  late TextEditingController _bioController;
  late TextEditingController _genderController;
  late TextEditingController _addressController;

  @override
  void initState() {
    super.initState();
    _nameController = TextEditingController(text: widget.user.name);
    _emailController = TextEditingController(text: widget.user.email);
    _birthdayController = TextEditingController(text: widget.user.birthday);
    _bioController = TextEditingController(text: widget.user.bio);
    _genderController = TextEditingController(text: widget.user.gender);
    _addressController = TextEditingController(text: widget.user.address);
  }

  @override
  void dispose() {
    _nameController.dispose();
    _emailController.dispose();
    _birthdayController.dispose();
    _bioController.dispose();
    _genderController.dispose();
    _addressController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Edit Profile'),
      ),
      body: SingleChildScrollView(
        child: Padding(
          padding: const EdgeInsets.all(16.0),
          child: Card(
            child: Padding(
              padding: const EdgeInsets.all(16.0),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  TextField(
                    controller: _nameController,
                    decoration: const InputDecoration(
                      labelText: 'Name',
                    ),
                  ),
                  const SizedBox(height: 12.0),
                  TextField(
                    controller: _emailController,
                    decoration: const InputDecoration(
                      labelText: 'Email',
                    ),
                  ),
                  const SizedBox(height: 12.0),
                  TextField(
                    controller: _bioController,
                    decoration: const InputDecoration(
                      labelText: 'Bio',
                    ),
                    maxLines: 3,
                  ),
                  const SizedBox(height: 12.0),
                  TextField(
                    controller: _birthdayController,
                    decoration: const InputDecoration(
                      labelText: 'Date of Birth',
                    ),
                  ),
                  const SizedBox(height: 12.0),
                  TextField(
                    controller: _genderController,
                    decoration: const InputDecoration(
                      labelText: 'Gender',
                    ),
                  ),
                  const SizedBox(height: 12.0),
                  TextField(
                    controller: _addressController,
                    decoration: const InputDecoration(
                      labelText: 'Address',
                    ),
                    maxLines: 2,
                  ),
                  const SizedBox(height: 16.0),
                  ElevatedButton(
                    onPressed: () {
                      // Update the user's profile with the new information
                      updateUserProfile();
                      // Navigate back to the ProfileScreen
                      Navigator.pop(context);
                    },
                    child: const Text('Save'),
                  ),
                ],
              ),
            ),
          ),
        ),
      ),
    );
  }

  void updateUserProfile() {
    // Update the user's profile with the new information
    // You can use the updated values from the TextEditingControllers
    // and update the user object or call a function to save the changes
  }
}