import 'package:flutter/material.dart';

Column buildInputField(String label, TextEditingController controller) {
  return Column(
    crossAxisAlignment: CrossAxisAlignment.start,
    children: [
      Text(
        '$label: ',
        style: const TextStyle(
          fontWeight: FontWeight.bold,
          fontSize: 18.0,
        ),
      ),
      Container(
        height: 50,
        margin: const EdgeInsets.only(bottom: 10.0),
        width: double.infinity,
        child: TextFormField(
          decoration: InputDecoration(
            hintText: 'Enter $label',
          ),
          controller: controller,
        ),
      ),
    ],
  );
}