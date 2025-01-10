import 'dart:convert';

import 'package:flutterapp/core/network/api_urls.dart';
import 'package:flutterapp/core/network/session.dart';
import 'package:flutterapp/features/forums/data/models/Discussion_model.dart';
import 'package:flutterapp/features/forums/data/models/forum_group_model.dart';

import '../../../../core/exceptions/error.dart';
import '../models/forum_model.dart';
import 'package:http/http.dart' as http;

abstract class ForumDataSource {
  Future<List<ForumModel>> getAllForum();

  Future<List<ForumModel>> getAllForumByGroup(int groupId);

  Future<List<ForumsGroupModel>> getAllForumGroup(); //top heading

  Future<List<DiscussionAllModel>> getAllDiscussions(
      String title); //search page
}

const uri = '${ApiUrls.API_BASE_URL}/mobile/forum';
const uri2 = '${ApiUrls.API_BASE_URL}/mobile/discussions';

class ForumDataSourceImp implements ForumDataSource {
  final NetworkService client;

  ForumDataSourceImp({required this.client});
  //---------------------------------------------------------

  @override
  Future<List<ForumModel>> getAllForum() async {
    List<ForumModel> forums = [];
    try {
      http.Response res = await client.get('$uri/forums-info/all');
      List jsonResponse = json.decode(res.body);
      print(res.body);
      if (res.statusCode == 200) {
        forums = jsonResponse.map((e) => ForumModel.fromJson(e)).toList();
      } else {
        throw ServerException('Error getAllForum');
      }
    } catch (err) {
      print(err.toString());
      throw ServerException(err.toString());
    }
    return forums;
  }

  //---------------------------------------------------------

  @override
  Future<List<ForumModel>> getAllForumByGroup(int id) async {
    List<ForumModel> forums = [];
    try {
      http.Response res = await client.get('$uri/forums-info/$id');
      List jsonResponse = json.decode(res.body);
      print(res.body);
      if (res.statusCode == 200) {
        forums = jsonResponse.map((e) => ForumModel.fromJson(e)).toList();
      } else {
        throw ServerException('Error getAllForumByGroup');
      }
    } catch (err) {
      print('Error getAllForumByGroup: ${err.toString()}');
      throw ServerException(err.toString());
    }
    return forums;
  }

  //---------------------------------------------------------
  @override
  Future<List<ForumsGroupModel>> getAllForumGroup() async {
    print("Getting all forum groups");
    List<ForumsGroupModel> groups = [];
    try {
      http.Response res = await client.get('$uri/groups');
      List jsonResponse = json.decode(res.body);
      print(res.body);
      if (res.statusCode == 200) {
        print("Found groups");
        groups = jsonResponse.map((e) => ForumsGroupModel.fromMap(e)).toList();
        print(groups.length);
      } else {
        print("Error other getting groups");
        throw ServerException('Server error');
      }
    } catch (err) {
      print("Error getting groups");
      print(err.toString());
      throw ServerException(err.toString());
    }
    print(groups.length);
    return groups;
  }

  @override
  Future<List<DiscussionAllModel>> getAllDiscussions(String title) async {
    print("Getting all discussions");
    List<DiscussionAllModel> discussions = [];
    try {
      http.Response res = await client.get('$uri2/all?title=$title');
      List jsonResponse = json.decode(res.body);
      print(res.body);

      if (res.statusCode == 200) {
        print("Found discussions");
        discussions =
            jsonResponse.map((e) => DiscussionAllModel.fromMap(e)).toList();
      } else {
        throw ServerException('Error getAllDiscussions');
      }
    } catch (err) {
      print(err.toString());
      throw ServerException(err.toString());
    }
    return discussions;
  }
}