import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:flutterapp/features/auth/presentation/views/login_screen.dart';
import 'package:flutterapp/features/auth/presentation/views/welcome_screen.dart';

import '../bloc/auth_bloc.dart';

class RegisterScreen extends StatefulWidget {
  const RegisterScreen({super.key});

  @override
  State<RegisterScreen> createState() => _RegisterScreenState();
}

class _RegisterScreenState extends State<RegisterScreen> {
  final emailController = TextEditingController();
  final passwordController = TextEditingController();
  final usernameController = TextEditingController();
  final passwordConfirmController = TextEditingController();
  bool _hidePassword = true;
  bool _hidePasswordConfirm = true;
  bool _isFocusedUsername = false;
  bool _isFocusedEmail = false;
  bool _isFocusedPassword = false;
  bool _isFocusedPasswordConfirm = false;
  String? _usernameError;
  String? _emailError;
  String? _passwordError;
  String? _passwordConfirmError;

  void _validateAndRegister() {
    const emailPattern = r'^[^@]+@gmail\.com$';
    final emailRegExp = RegExp(emailPattern);

    setState(() {
      _emailError = emailController.text.isEmpty
          ? 'Email cannot be empty'
          : (!emailRegExp.hasMatch(emailController.text)
              ? 'Enter a valid email address'
              : null);
      _passwordError =
          passwordController.text.isEmpty ? 'Password cannot be empty' : null;

      _usernameError =
          usernameController.text.isEmpty ? 'Username cannot be empty' : null;

      _passwordConfirmError = passwordConfirmController.text.isEmpty
          ? 'Password cannot be empty'
          : (passwordController.text != passwordConfirmController.text
              ? 'Password does not match'
              : null);
    });

    if (_emailError == null &&
        _passwordError == null &&
        _usernameError == null &&
        _passwordConfirmError == null) {
      context.read<AuthBloc>().add(Register(
          username: usernameController.text,
          email: emailController.text,
          password: passwordController.text));
      //to login
      // Navigator.pop(context);
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Stack(
        children: [
          // Background image
          Container(
            decoration: const BoxDecoration(
              image: DecorationImage(
                image: AssetImage('assets/images/register_background.png'),
                fit: BoxFit.none,
              ),
            ),
          ),
          Center(
            child: BlocListener<AuthBloc, AuthState>(
              listener: (context, state) {
                if (state is RegisterSuccess) {
                  Navigator.of(context).pushReplacement(
                    MaterialPageRoute(
                      builder: (context) => const LoginScreen(),
                    ),
                  );
                  ScaffoldMessenger.of(context).showSnackBar(
                    const SnackBar(
                      content: Text(
                        'Registered successfully',
                        style: TextStyle(color: Colors.green),
                      ),
                    ),
                  );
                } else if (state is RegisterFailure) {
                  ScaffoldMessenger.of(context).showSnackBar(
                    SnackBar(
                      content: Text(
                        state.message,
                        style: const TextStyle(color: Colors.red),
                      ),
                    ),
                  );
                }
              },
              child: SingleChildScrollView(
                padding: const EdgeInsets.all(20.0),
                child: Column(
                  mainAxisAlignment: MainAxisAlignment.center,
                  children: [
                    const Text(
                      'Register',
                      style: TextStyle(
                        color: Colors.white,
                        fontSize: 24.0,
                        fontWeight: FontWeight.bold,
                      ),
                    ),
                    const SizedBox(height: 10.0),
                    TextField(
                      controller: usernameController,
                      keyboardType: TextInputType.text,
                      decoration: InputDecoration(
                        hintText: 'Enter your username',
                        hintStyle: const TextStyle(color: Colors.white),
                        filled: true,
                        fillColor: _isFocusedUsername
                            ? const Color.fromARGB(156, 104, 151, 187)
                            : const Color.fromARGB(172, 110, 114, 117),
                        border: const OutlineInputBorder(
                          borderRadius: BorderRadius.all(Radius.circular(8.0)),
                          borderSide: BorderSide.none,
                        ),
                        errorText: _usernameError,
                      ),
                      onTap: () {
                        setState(() {
                          _isFocusedUsername = true;
                        });
                      },
                      onEditingComplete: () {
                        setState(() {
                          _isFocusedUsername = false;
                        });
                      },
                    ),
                    const SizedBox(height: 10.0),
                    TextField(
                      controller: emailController,
                      keyboardType: TextInputType.emailAddress,
                      decoration: InputDecoration(
                        hintText: 'Enter your email',
                        hintStyle: const TextStyle(color: Colors.white),
                        filled: true,
                        fillColor: _isFocusedEmail
                            ? const Color.fromARGB(156, 104, 151, 187)
                            : const Color.fromARGB(172, 110, 114, 117),
                        border: const OutlineInputBorder(
                          borderRadius: BorderRadius.all(Radius.circular(8.0)),
                          borderSide: BorderSide.none,
                        ),
                        errorText: _emailError,
                      ),
                      onTap: () {
                        setState(() {
                          _isFocusedEmail = true;
                        });
                      },
                      onEditingComplete: () {
                        setState(() {
                          _isFocusedEmail = false;
                        });
                      },
                    ),
                    const SizedBox(height: 10.0),
                    TextField(
                      obscureText: _hidePassword,
                      controller: passwordController,
                      decoration: InputDecoration(
                        hintText: 'Enter your password',
                        hintStyle: const TextStyle(color: Colors.white),
                        filled: true,
                        fillColor: _isFocusedPassword
                            ? const Color.fromARGB(156, 104, 151, 187)
                            : const Color.fromARGB(172, 110, 114, 117),
                        border: const OutlineInputBorder(
                          borderRadius: BorderRadius.all(Radius.circular(8.0)),
                          borderSide: BorderSide.none,
                        ),
                        suffixIcon: IconButton(
                          icon: Icon(_hidePassword
                              ? Icons.visibility_off
                              : Icons.visibility),
                          onPressed: () {
                            setState(() {
                              _hidePassword = !_hidePassword;
                            });
                          },
                        ),
                        errorText: _passwordError,
                      ),
                      onTap: () {
                        setState(() {
                          _isFocusedPassword = true;
                        });
                      },
                      onEditingComplete: () {
                        setState(() {
                          _isFocusedPassword = false;
                        });
                      },
                    ),
                    const SizedBox(height: 16.0),
                    TextField(
                      obscureText: _hidePasswordConfirm,
                      controller: passwordConfirmController,
                      decoration: InputDecoration(
                        hintText: 'Confirm your password',
                        hintStyle: const TextStyle(color: Colors.white),
                        filled: true,
                        fillColor: _isFocusedPasswordConfirm
                            ? const Color.fromARGB(156, 104, 151, 187)
                            : const Color.fromARGB(172, 110, 114, 117),
                        border: const OutlineInputBorder(
                          borderRadius: BorderRadius.all(Radius.circular(8.0)),
                          borderSide: BorderSide.none,
                        ),
                        suffixIcon: IconButton(
                          icon: Icon(_hidePasswordConfirm
                              ? Icons.visibility_off
                              : Icons.visibility),
                          onPressed: () {
                            setState(() {
                              _hidePasswordConfirm = !_hidePasswordConfirm;
                            });
                          },
                        ),
                        errorText: _passwordConfirmError,
                      ),
                      onTap: () {
                        setState(() {
                          _isFocusedPasswordConfirm = true;
                        });
                      },
                      onEditingComplete: () {
                        setState(() {
                          _isFocusedPasswordConfirm = false;
                        });
                      },
                    ),
                    const SizedBox(height: 16.0),
                    ElevatedButton(
                      onPressed: _validateAndRegister,
                      style: ElevatedButton.styleFrom(
                        backgroundColor: Colors.white,
                        foregroundColor: Colors.black,
                        shape: RoundedRectangleBorder(
                          borderRadius: BorderRadius.circular(8.0),
                        ),
                      ),
                      child: const Text('REGISTER'),
                    ),
                    const SizedBox(height: 10.0),
                    TextButton(
                      onPressed: () {
                        Navigator.of(context).pop(
                          MaterialPageRoute(
                            builder: (context) => const LoginScreen(),
                          ),
                        );
                      },
                      child: const Text(
                        'Already have an account? Login here.',
                        style: TextStyle(
                          color: Colors.white,
                          fontSize: 16.0,
                          fontWeight: FontWeight.bold,
                        ),
                      ),
                    )
                  ],
                ),
              ),
            ),
          )
        ],
      ),
    );
  }
}