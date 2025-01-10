import 'dart:convert';
import 'dart:io';
import 'package:flutterapp/core/exceptions/error.dart';
import 'package:flutterapp/core/storage/storage.dart';
import 'package:flutterapp/core/network/api_urls.dart';
import 'package:flutterapp/features/auth/data/models/user_model.dart';
import 'package:http/http.dart' as http;

import '../../../../core/network/session.dart';

abstract class AuthDataSource {
  Future<String> handleRegister(
    String email,
    String password,
    String username,
  );
  Future<String> handleLogin(String email, String password);

  Future<void> logout();

  Future<UserModel> getUserInfo(String ownerId);
  Future<String> changeProfilePic(File file);
}

const uri = '${ApiUrls.API_BASE_URL}/auth';

class AuthDataSourceImp implements AuthDataSource {
  // final http.Client client;
  final NetworkService client;

  AuthDataSourceImp({required this.client});
  //---------------------------------------------------------

  @override
  Future<String> handleRegister(
    String email,
    String password,
    String username,
  ) async {
    String response = "Unexpected error";
    try {
      http.Response res = await client.post(
          '$uri/signup',
          jsonEncode({
            'email': email,
            'password': password,
            'username': username,
          }));

      Map jsonResponse = json.decode(res.body);
      // print(res.body);
      if (res.statusCode == 200) {
        print("User created successfully");
        return "User created successfully";
      } else {
        print("User creation failed");
        throw Exception();
      }
    } catch (err) {
      print("User creation failed: $err");
      response = err.toString();
    }
    return response;
  }

//---------------------------------------------------------
  @override
  Future<String> handleLogin(String email, String password) async {
    http.Response res = await client.post(
        '$uri/signin',
        jsonEncode({
          'username': email,
          'password': password,
        }));

    Map jsonResponse = json.decode(res.body);
    // print(res.body);

    if (res.statusCode == 200) {
      String userId = jsonResponse['username'];
      _saveUserId(userId);
      return jsonResponse['accessToken'];
    } else {
      throw ServerException('Unexpected error');
    }
  }

  //---------------------------------------------------------
  @override
  Future<void> logout() {
    // TODO: implement logout
    throw UnimplementedError();
  }
  //---------------------------------------------------------

  Future<void> _saveUserId(String userId) async {
    await Storage().secureStorage.write(key: 'userId', value: userId);
  }

  Future<void> _saveAvatar(String avatar) async {
    await Storage().secureStorage.write(key: 'avatarUrl', value: avatar);
  }

  //---------------------------------------------------------

  @override
  Future<UserModel> getUserInfo(String ownerId) async {
    http.Response res = await http.get(
      Uri.parse('$uri/user/$ownerId'),
      headers: <String, String>{
        'Content-Type': 'application/json; charset=UTF-8',
      },
    );
    Map jsonResponse = json.decode(res.body);
    print(res.body);

    if (res.statusCode == 200) {
      return UserModel.fromJson(jsonResponse);
    } else {
      throw Exception();
    }
  }
  //---------------------------------------------------------

  @override
  Future<String> changeProfilePic(File file) async {
    String res = "Unexpected error";
    try {
      String? ownerId = await Storage().secureStorage.read(key: 'userId');

      if (ownerId == null) {
        throw Exception();
      }

      http.MultipartRequest request = http.MultipartRequest(
        'POST',
        Uri.parse('$uri/user/$ownerId/image'),
      );
      request.files.add(
        await http.MultipartFile.fromPath(
          'image',
          file.path,
        ),
      );
      http.StreamedResponse res = await request.send();
      String resStr = await res.stream.bytesToString();
      print(resStr);
      if (res.statusCode == 200) {
        return resStr;
      } else {
        throw Exception();
      }
    } catch (err) {
      res = err.toString();
    }
    return res;
  }

  //---------------------------------------------------------
}