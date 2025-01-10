import 'package:dartz/dartz.dart';

import '../../../../core/exceptions/failure.dart';
import '../../../../core/usecases/change_img_prof.dart';
import '../repository/auth_repo.dart';

class ChangeProImg implements ChangeProImgUseCase<void, ParamsChangeProImg> {
  final AuthRepo repository;

  ChangeProImg({required this.repository});

  @override
  Future<Either<Failure, String>> call(ParamsChangeProImg params) async {
    return await repository.changeProfImg(file: params.profileImage);
  }
}