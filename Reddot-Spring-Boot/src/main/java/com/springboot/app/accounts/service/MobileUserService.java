package com.springboot.app.accounts.service;

import com.springboot.app.accounts.dto.request.AccountInfo;
import com.springboot.app.accounts.dto.responce.MobileMemberResponse;
import com.springboot.app.accounts.dto.responce.MobileUserInfoResponse;
import com.springboot.app.dto.response.ServiceResponse;

import java.util.List;

public interface MobileUserService {

    ServiceResponse<List<MobileMemberResponse>> getAllMembers();

    ServiceResponse<List<MobileMemberResponse>> getMembersBy(String search);

    ServiceResponse<MobileUserInfoResponse> getMemberByUsername(String username);

    ServiceResponse<MobileUserInfoResponse> updateMemberByUsername(AccountInfo userInfo);
}
