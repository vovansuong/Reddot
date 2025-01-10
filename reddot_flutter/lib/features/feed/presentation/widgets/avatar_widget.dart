import 'package:flutter/material.dart';
import 'package:flutterapp/core/network/api_urls.dart';

Widget buildAvatar(
    {String imageUrl = '', String avatar = '', double? width, double? height}) {
  late String imagePath = '';
  if (imageUrl != '') {
    imagePath = imageUrl;
  } else if (avatar != '') {
    imagePath = '${ApiUrls.avatarUrl}/${avatar}';
  } else {
    imagePath =
        'https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_1280.png';
  }
  final image = NetworkImage(imagePath);
  return ClipOval(
    child: Material(
      color: Colors.transparent,
      child: Ink.image(
        image: image,
        fit: BoxFit.cover,
        width: width,
        height: height,
      ),
    ),
  );
}