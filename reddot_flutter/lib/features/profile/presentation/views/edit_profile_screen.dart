import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:flutterapp/core/usecases/profile/update_info_core.dart';
import 'package:flutterapp/features/profile/data/models/user_pre_model.dart';
import 'package:flutterapp/features/profile/domain/entities/user_pro_entity.dart';
import 'package:flutterapp/features/profile/presentation/bloc/profile_bloc.dart';
import 'package:intl/intl.dart';

class EditProfileScreen extends StatefulWidget {
  final UserProEntity user;

  const EditProfileScreen({super.key, required this.user});

  @override
  _EditProfileScreenState createState() => _EditProfileScreenState();
}

class _EditProfileScreenState extends State<EditProfileScreen> {
  late TextEditingController _nameController;
  late TextEditingController _emailController;
  late TextEditingController _phoneController;
  late TextEditingController _bioController;
  late TextEditingController _genderController;
  late TextEditingController _addressController;
  DateTime? _selectedDate;

  String? _nameError;
  String? _emailError;
  String? _phoneError;
  String? _bioError;
  String? _genderError;
  String? _addressError;
  String? _dateError;

  @override
  void initState() {
    super.initState();
    _nameController = TextEditingController(text: widget.user.name);
    _phoneController = TextEditingController(text: "1234567890");
    _emailController = TextEditingController(text: widget.user.email);
    _bioController = TextEditingController(text: widget.user.bio);
    _genderController = TextEditingController(text: widget.user.gender);
    _addressController = TextEditingController(text: widget.user.address);
  }

  void _presentDatePicker() {
    showDatePicker(
      context: context,
      initialDate: DateTime.now(),
      firstDate: DateTime(2024),
      lastDate: DateTime.now(),
    ).then((pickedDate) {
      if (pickedDate == null) {
        return;
      }
      setState(() {
        _selectedDate = pickedDate;
      });
    });
  }

  void _validateUpdateProfile() {
    setState(() {
      _nameError = _nameController.text.isEmpty ? 'Name cannot be empty' : null;
      _emailError =
          _emailController.text.isEmpty ? 'Email cannot be empty' : null;
      _phoneError =
          _phoneController.text.isEmpty ? 'Phone cannot be empty' : null;
      _bioError = _bioController.text.isEmpty ? 'Bio cannot be empty' : null;
      _genderError =
          _genderController.text.isEmpty ? 'Gender cannot be empty' : null;
      _addressError =
          _addressController.text.isEmpty ? 'Address cannot be empty' : null;
      _dateError = _selectedDate == null ? 'Date cannot be empty' : null;
    });

    if (_nameError != null &&
        _emailError != null &&
        _addressError != null &&
        _bioError != null &&
        _genderError != null &&
        _phoneError != null &&
        _dateError != null) {
      // Update the user's profile with the new information
      context.read<ProfileBloc>().add(UpdateProfileEvent(
            userPro: ParamsEditUserPro(
                username: widget.user.username,
                name: _nameController.text,
                phone: _phoneController.text,
                email: _emailController.text,
                bio: _bioController.text,
                birthday: _selectedDate,
                address: _addressController.text,
                gender: _genderController.text),
          ));
      // Navigate back to the ProfileScreen
      Navigator.pop(context);
    }
  }

  @override
  void dispose() {
    _nameController.dispose();
    _phoneController.dispose();
    _emailController.dispose();
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
                    decoration: InputDecoration(
                      labelText: 'Name',
                      errorText: _nameError,
                    ),
                  ),
                  const SizedBox(height: 12.0),
                  TextField(
                    controller: _emailController,
                    decoration: InputDecoration(
                      labelText: 'Email',
                      errorText: _emailError,
                    ),
                  ),
                  const SizedBox(height: 12.0),
                  TextField(
                    controller: _bioController,
                    decoration: InputDecoration(
                      labelText: 'Bio',
                      errorText: _bioError,
                    ),
                    maxLines: 3,
                  ),
                  const SizedBox(height: 12.0),
                  SizedBox(
                    height: 70,
                    child: Row(
                      children: <Widget>[
                        Expanded(
                          child: Text(_selectedDate == null
                              ? 'No Date Chosen!'
                              : 'Birthday: ${DateFormat.yMd().format(_selectedDate!)}'),
                        ),
                        TextButton(
                          onPressed: _presentDatePicker,
                          child: const Text(
                            'Choose Date',
                            style: TextStyle(fontWeight: FontWeight.bold),
                          ),
                        ),
                      ],
                    ),
                  ),
                  if (_dateError != null)
                    Padding(
                      padding: const EdgeInsets.only(top: 1.0),
                      child: Text(
                        _dateError!,
                        style: const TextStyle(
                          color: Color.fromARGB(255, 244, 165, 160),
                          fontSize: 12.0,
                        ),
                      ),
                    ),
                  const SizedBox(height: 12.0),
                  TextField(
                    controller: _genderController,
                    decoration: InputDecoration(
                      labelText: 'Gender',
                      errorText: _genderError,
                    ),
                  ),
                  const SizedBox(height: 12.0),
                  TextField(
                    controller: _addressController,
                    decoration: InputDecoration(
                      labelText: 'Address',
                      errorText: _addressError,
                    ),
                    maxLines: 2,
                  ),
                  const SizedBox(height: 16.0),
                  ElevatedButton(
                    onPressed: _validateUpdateProfile,
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
