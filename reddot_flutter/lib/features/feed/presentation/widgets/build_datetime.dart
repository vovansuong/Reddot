import 'package:flutter/material.dart';
import 'package:intl/intl.dart';

Widget buildCreatedAt(String username, DateTime createdAt) {
  DateTime at = createdAt ?? DateTime.now();
  String name = username ?? "Anonymous";
  return Text(
    '@$name created at: ${DateFormat('dd-MM-yyyy HH:mm').format(at)}',
    style: const TextStyle(
      fontSize: 14.0,
    ),
  );
}