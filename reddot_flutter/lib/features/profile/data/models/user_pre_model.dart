class User {
  final String imagePath;
  final String name;
  final String email;
  final String bio;
  final String about;
  final String birthday;
  final String address;
  final String gender;
  final List<String> favoritePosts;
  final bool isDarkMode;

  const User({
    required this.imagePath,
    required this.name,
    required this.email,
    required this.bio,
    required this.about,
    required this.birthday,
    required this.address,
    required this.gender,
    required this.favoritePosts,
    required this.isDarkMode,
  });
  List<String> get getFavoritePosts {
    return favoritePosts;
  }
}
