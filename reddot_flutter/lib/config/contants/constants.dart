import 'package:flutter/material.dart';

class TextConstants {
  static const String titleTab_1 = 'All';
  static const String titleTab_2 = 'Developer';
  static const String titleTab_3 = 'Financial';
  static const String titleTab_4 = 'Marketing';

  static const String HOME_SCREEN = "HOME PAGE";
  static const String LOGIN_SCREEN = "LOGIN SCREEN";
  static const String REGISTER_SCREEN = "REGISTER SCREEN";
  static const String USER_LIST = "USER LIST";

  static const String REGX_EMAIL = r'^[\w-\.]+@([\w-]+\.)+[\w-]{2,4}$';
  static const String REGX_PASSWORD = r'^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,}$';
  static const String REGX_NAME = r'^[a-zA-Z]{3,}$';
}