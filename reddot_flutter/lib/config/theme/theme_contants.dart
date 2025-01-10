import 'package:flutter/material.dart';
import 'package:flutterapp/config/theme/app_colors.dart';

final ThemeData lightTheme = ThemeData(
    listTileTheme: const ListTileThemeData(
      iconColor: Colors.black,
    ),
    brightness: Brightness.light,
    // primarySwatch: Colors.white,
    primaryColor: Colors.black,
    iconTheme: const IconThemeData(color: Colors.black),
    appBarTheme: const AppBarTheme(
      backgroundColor: Colors.white,
    ),
    textTheme: TextTheme(),
    colorScheme: const ColorScheme.light(primary: Colors.black)
        .copyWith(background: Colors.white));

final ThemeData darkTheme = ThemeData(
    brightness: Brightness.dark,
    primarySwatch: Colors.grey,
    textTheme: const TextTheme(
        bodyLarge: TextStyle(
      color: AppColors.textWhite,
    )),
    cardTheme: const CardTheme(color: Colors.white38),
    floatingActionButtonTheme: const FloatingActionButtonThemeData(
      backgroundColor: Colors.grey,
    ));
