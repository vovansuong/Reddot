import 'dart:convert';

import 'package:flutterapp/features/forums/domain/entities/forum_group_entity.dart';

class ForumsGroupModel extends ForumGroupEntity {
  const ForumsGroupModel(
      {required super.id, required super.title, required super.color});

  factory ForumsGroupModel.fromMap(Map<String, dynamic> json) {
    return ForumsGroupModel(
      id: (json["id"] ?? 0) as int,
      title: (json["title"] ?? "") as String,
      color: (json["color"] ?? "") as String,
    );
  }

  factory ForumsGroupModel.fromJson(source) =>
      ForumsGroupModel.fromMap(json.decode(source));
}