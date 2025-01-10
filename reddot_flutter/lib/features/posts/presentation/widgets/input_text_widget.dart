import 'package:flutter/material.dart';

Column buildInputFieldTextArea(String label, TextEditingController controller) {
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
        height: 200,
        margin: const EdgeInsets.only(bottom: 10.0),
        width: double.infinity,
        child: TextField(
          keyboardType: TextInputType.multiline,
          maxLines: 10,
          decoration: InputDecoration(
            hintText: 'Enter $label',
          ),
          controller: controller,
        ),
      ),
    ],
  );
}