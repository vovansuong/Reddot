import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutterapp/config/theme/theme_manager.dart';
import 'package:provider/provider.dart';

AppBar buildAppBar(BuildContext context) {
  final icon = CupertinoIcons.moon_stars;

  return AppBar(
    backgroundColor: Colors.transparent,
    elevation: 0,
    iconTheme: Theme.of(context).iconTheme,
    actions: [
      Consumer<ThemeService>(builder: (context, ThemeService theme, _) {
        return IconButton(
            onPressed: () {
              theme.toggleTheme();
            },
            icon: Icon(
                theme.darkTheme! ? Icons.sunny : CupertinoIcons.moon_stars));
      })
    ],
    bottom: PreferredSize(
      preferredSize: const Size.fromHeight(50),
      child: Padding(
        padding: const EdgeInsets.all(8.0),
        child: TextField(
          decoration: InputDecoration(
            hintText: 'Search',
            hintStyle: const TextStyle(color: Colors.black),
            prefixIcon: const Icon(
              Icons.search,
              color: Colors.black,
            ),
            border: OutlineInputBorder(
              borderRadius: BorderRadius.circular(10),
            ),
            filled: true,
            fillColor: Colors.white,
          ),
          style: const TextStyle(color: Colors.black),
          onChanged: (value) {
            // Timer(const Duration(seconds: 1), () => _onSearch(value));
            // _onSearch(value);
          },
        ),
      ),
    ),
  );
}