import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:flutterapp/features/auth/domain/entities/user_entity.dart';
import 'package:flutterapp/features/auth/presentation/views/register_screen.dart';
import 'package:flutterapp/features/auth/presentation/views/welcome_screen.dart';
import 'package:flutterapp/features/feed/presentation/views/main_screen.dart';
import 'package:formz/formz.dart';

import '../bloc/auth_bloc.dart';

class LoginScreen extends StatefulWidget {
  const LoginScreen({super.key});

  @override
  State<LoginScreen> createState() => _LoginScreenState();
}

class _LoginScreenState extends State<LoginScreen> {
  late var emailController = TextEditingController();
  late final passwordController = TextEditingController();
  bool _hidePassword = true;
  bool _isFocusedEmail = false;
  bool _isFocusedPassword = false;
  String? _emailError;
  String? _passwordError;

  late MyFormState _state;
  void _onEmailChanged() {
    setState(() {
      _state = _state.copyWith(email: Email.dirty(emailController.text));
    });
  }

  void _onPasswordChanged() {
    setState(() {
      _state =
          _state.copyWith(password: Password.dirty(passwordController.text));
    });
  }

  @override
  void initState() {
    super.initState();
    _state = MyFormState();
    emailController = TextEditingController(text: _state.email.value)
      ..addListener(_onEmailChanged);
    passwordController.addListener(_onPasswordChanged);
  }

  @override
  void dispose() {
    emailController.dispose();
    passwordController.dispose();
    super.dispose();
  }

  void _validateAndLogin() {
    const emailPattern = r'^[^@]+@[^@]+\.[^@]+';
    final emailRegExp = RegExp(emailPattern);

    setState(() {
      if (emailController.text.contains('@')) {
        _emailError = emailController.text.isEmpty
            ? 'Email cannot be empty'
            : (!emailRegExp.hasMatch(emailController.text)
                ? 'Enter a valid email address'
                : null);
      } else {
        _emailError =
            emailController.text.isEmpty ? 'Username cannot be empty' : null;
      }
      _passwordError =
          passwordController.text.isEmpty ? 'Password cannot be empty' : null;
    });

    if (_emailError == null && _passwordError == null) {
      context.read<AuthBloc>().add(LoggedIn(
            email: emailController.text,
            password: passwordController.text,
          ));
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Stack(
        children: [
          // Background image
          Positioned.fill(
            child: Image.asset(
              'assets/images/login_background.png',
              fit: BoxFit.cover,
            ),
          ),
          // Login form
          Center(
            child: BlocListener<AuthBloc, AuthState>(
              listener: (context, state) {
                if (state is Authenticated) {
                  Navigator.of(context).pushReplacement(
                    MaterialPageRoute(
                      builder: (context) => const MainScreen(),
                    ),
                  );
                } else if (state is LoginFailure) {
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
              child: ConstrainedBox(
                constraints: const BoxConstraints(maxWidth: 400),
                child: Padding(
                  padding: const EdgeInsets.all(20.0),
                  child: Column(
                    mainAxisAlignment: MainAxisAlignment.center,
                    children: [
                      const Text(
                        'Sign in',
                        //style: Theme.of(context).textTheme.headlineSmall,
                        style: TextStyle(
                          color: Colors.white,
                          fontSize: 24.0,
                          fontWeight: FontWeight.bold,
                        ),
                      ),
                      const SizedBox(height: 16.0),
                      TextButton(
                        onPressed: () {
                          Navigator.of(context).push(
                            MaterialPageRoute(
                              builder: (context) => const RegisterScreen(),
                            ),
                          );
                        },
                        child: const Text(
                          'Don\'t have an account? Register here',
                          style: TextStyle(
                              fontSize: 18.0,
                              color: Color.fromARGB(255, 226, 98, 88)),
                          textAlign: TextAlign.center,
                        ),
                      ),
                      const SizedBox(height: 16.0),
                      TextFormField(
                        controller: emailController,
                        keyboardType: TextInputType.emailAddress,
                        decoration: InputDecoration(
                          hintText: 'Enter your username or email',
                          filled: true,
                          fillColor: _isFocusedEmail
                              ? const Color.fromARGB(143, 55, 108, 148)
                              : const Color.fromARGB(93, 55, 108, 148),
                          border: const OutlineInputBorder(
                            borderRadius:
                                BorderRadius.all(Radius.circular(8.0)),
                            borderSide: BorderSide.none,
                          ),
                          errorText: _emailError,
                        ),
                        // validator: (value) =>
                        // _state.email.validator(value ?? '').text(),
                        onTap: () {
                          setState(() {
                            _isFocusedEmail = true;
                            _isFocusedPassword = false;
                          });
                        },
                        onEditingComplete: () {
                          setState(() {
                            _isFocusedEmail = false;
                          });
                        },
                      ),
                      const SizedBox(height: 18.0),
                      TextField(
                        obscureText: _hidePassword,
                        controller: passwordController,
                        decoration: InputDecoration(
                          hintText: 'Enter your password',
                          filled: true,
                          fillColor: _isFocusedPassword
                              ? const Color.fromARGB(143, 55, 108, 148)
                              : const Color.fromARGB(93, 55, 108, 148),
                          border: const OutlineInputBorder(
                            borderRadius:
                                BorderRadius.all(Radius.circular(8.0)),
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
                            _isFocusedEmail = false;
                            _isFocusedPassword = true;
                          });
                        },
                        onEditingComplete: () {
                          setState(() {
                            _isFocusedPassword = false;
                          });
                        },
                      ),
                      const SizedBox(height: 18.0),
                      ElevatedButton(
                        onPressed: _validateAndLogin,
                        child: const Text(
                          'SIGN IN',
                          style: TextStyle(
                            color: Colors.white,
                            fontSize: 16.0,
                            fontWeight: FontWeight.bold,
                          ),
                        ),
                      ),
                    ],
                  ),
                ),
              ),
            ),
          ),
        ],
      ),
    );
  }
}

class MyFormState with FormzMixin {
  MyFormState({
    Email? email,
    this.password = const Password.pure(),
    this.status = FormzSubmissionStatus.initial,
  }) : email = email ?? const Email.pure();

  final Email email;
  final Password password;
  final FormzSubmissionStatus status;

  MyFormState copyWith({
    Email? email,
    Password? password,
    FormzSubmissionStatus? status,
  }) {
    return MyFormState(
      email: email ?? this.email,
      password: password ?? this.password,
      status: status ?? this.status,
    );
  }

  @override
  List<FormzInput<dynamic, dynamic>> get inputs => [email, password];
}