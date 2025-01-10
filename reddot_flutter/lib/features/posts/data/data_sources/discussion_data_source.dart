import 'dart:convert';
import 'dart:io';
import 'package:flutterapp/core/network/api_urls.dart';
import 'package:flutterapp/features/posts/data/models/dis_res_model.dart';
import 'package:flutterapp/features/posts/domain/entities/dis_req_entity.dart';
import 'package:http/http.dart' as http;

import 'package:flutterapp/core/exceptions/error.dart';
import 'package:flutterapp/core/network/session.dart';

import '../models/comment_model.dart';

abstract class DiscussionDataSource {
  //create a new discussion
  Future<DiscussionResponseEntity?> createDiscussion(
      String title, String content, int forumId, String author);

  //create  a new comment
  Future<String?> createComment(
      String content, int discussionId, String author);

  //get all Comments
  Future<List<CommentModel>> getAllCommentsBy(int discussionId);
}

const uri = '${ApiUrls.API_BASE_URL}/mobile/forum';

class DiscussionDataSourceImpl implements DiscussionDataSource {
  final NetworkService client;

  DiscussionDataSourceImpl({required this.client});

  @override
  Future<List<CommentModel>> getAllCommentsBy(int id) async {
    List<CommentModel> comments = [];
    try {
      http.Response res = await client.get('$uri/discussions/$id');
      List jsonResponse = json.decode(res.body);
      print(res.body);
      if (res.statusCode == 200) {
        comments = jsonResponse.map((e) => CommentModel.fromJson(e)).toList();
      } else {
        throw ServerException('Error getAllCommentsBy');
      }
    } catch (err) {
      print('Error getAllCommentsBy: ${err.toString()}');
      throw ServerException(err.toString());
    }
    return comments;
  }

  @override
  Future<DiscussionResponseEntity?> createDiscussion(
      String title, String content, int forumId, String author) async {
    print("Create discussion in forum: $forumId");
    try {
      http.Response res = await client.post(
          '$uri/discussions/add',
          jsonEncode({
            'title': title,
            'content': content,
            'forumId': forumId,
            'author': author
          }));
      Map jsonResponse = json.decode(res.body);
      print(res.body);
      if (res.statusCode == 200) {
        print('DiscussionResponseModel created successfully');
        return DiscussionResponseModel.fromMap(jsonResponse);
      }
    } catch (err) {
      print('Error createDiscussion: ${err.toString()}');
      throw ServerException(err.toString());
    }
    return null;
  }

  @override
  Future<String?> createComment(
      String content, int discussionId, String author) async {
    try {
      http.Response res = await client.post(
          '$uri/comments/add',
          jsonEncode({
            'content': content,
            'discussionId': discussionId,
            'author': author
          }));
      Map jsonResponse = json.decode(res.body);
      print(res.body);
      if (res.statusCode == 200) {
        return "Add Comment Successfully";
      }
    } catch (err) {
      print('Error createComment: ${err.toString()}');
      throw ServerException(err.toString());
    }
    return null;
  }
}
