import 'package:dio/dio.dart';
import 'package:flutterapp/core/network/api_urls.dart';
import 'package:flutterapp/core/storage/storage.dart';
import 'package:shared_preferences/shared_preferences.dart';

const uri = '${ApiUrls.API_BASE_URL}/mobile/auth';

class DioNetworkService {
  final Dio _dio = Dio();

  DioNetworkService() {
    _dio.interceptors.add(InterceptorsWrapper(
      onRequest: (options, handler) async {
        // Add access token to request headers
        String? accessToken = await Storage().secureStorage.read(key: 'token');
        if (accessToken != null) {
          options.headers['Authorization'] = 'Bearer $accessToken';
        }
        return handler.next(options);
      },
      onError: (error, handler) async {
        // Handle token expiration
        if (error.response?.statusCode == 401) {
          RequestOptions requestOptions = error.requestOptions;
          // Assume refresh token logic is in place and get a new access token
          try {
            String? newAccessToken = await _refreshToken();
            if (newAccessToken != null) {
              // Save new access token
              _saveAccessToken(newAccessToken);
              // Retry the failed request with the new access token
              requestOptions.headers['Authorization'] =
                  'Bearer $newAccessToken';
              final response = await _dio.request(
                requestOptions.path,
                options: Options(
                  method: requestOptions.method,
                  headers: requestOptions.headers,
                ),
                data: requestOptions.data,
                queryParameters: requestOptions.queryParameters,
              );
              return handler.resolve(response);
            }
          } catch (e) {
            // Handle refresh token failure
            return handler.reject(error);
          }
        }
        return handler.reject(error);
      },
    ));
  }

  Dio get dio => _dio;

  Future<String?> _refreshToken() async {
    String? refreshToken =
        await Storage().secureStorage.read(key: 'refresh_token');

    try {
      final response = await _dio.post('$uri/refreshtoken', data: {
        'refreshToken': refreshToken,
      });

      if (response.statusCode == 200) {
        return response.data['accessToken'];
      }
    } catch (e) {
      // Handle errors (e.g., log them, rethrow them, etc.)
      print(e);
    }
    return null;
  }

  Future<void> _saveAccessToken(String accessToken) async {
    await Storage().secureStorage.write(key: 'token', value: accessToken);
  }

  Future<void> logout() async {
    await Storage().secureStorage.delete(key: 'token');
    await Storage().secureStorage.delete(key: 'refresh_token');
    await Storage().secureStorage.delete(key: 'userId');
  }
}