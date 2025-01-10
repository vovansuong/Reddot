import 'dart:convert';
import 'dart:io';
import 'package:flutterapp/core/usecases/profile/update_info_core.dart';
import 'package:http/http.dart' as http;

import 'package:flutterapp/core/exceptions/error.dart';
import 'package:flutterapp/core/network/api_urls.dart';
import 'package:flutterapp/core/network/session.dart';
import 'package:flutterapp/features/profile/data/models/user_pro_model.dart';

abstract class ProfileDataSource {
  Future<UserProModel> getUserProBy(String username);

  Future<UserProModel> updateInfo(ParamsEditUserPro params);
}

const uri = '${ApiUrls.API_BASE_URL}/mobile/member';

class ProfileDataSourceImpl implements ProfileDataSource {
  final NetworkService client;

  ProfileDataSourceImpl({required this.client});

  @override
  Future<UserProModel> getUserProBy(String username) async {
    try {
      http.Response res = await client.get('$uri/$username');
      final data = json.decode(res.body);
      print(data);
      if (res.statusCode != 200) {
        throw ServerException('Error getUserProBy');
      }
      final userPro = UserProModel.fromJson(data);
      return userPro;
    } catch (err) {
      print('Error getUserProBy: ${err.toString()}');
      throw ServerException(err.toString());
    }
  }

  @override
  Future<UserProModel> updateInfo(ParamsEditUserPro params) async {
    try {
      http.Response res = await client.post(
        '$uri/update',
        jsonEncode({
          'username': params.username,
          'name': params.name,
          'email': params.email,
          'phone': params.phone,
          'address': params.address,
          'gender': params.gender,
          'bio': params.bio,
          'birthday': params.birthday,
        }),
      );
      final data = json.decode(res.body);
      print(res.body);
      if (res.statusCode != 200) {
        throw ServerException('Error updateInfo');
      }
      final userPro = UserProModel.fromJson(data);
      return userPro;
    } catch (err) {
      print('Error updateInfo: ${err.toString()}');
      throw ServerException(err.toString());
    }
  }
}