package com.springboot.app.accounts.service;

import com.springboot.app.accounts.dto.request.AccountInfo;
import com.springboot.app.accounts.dto.responce.AccountInfoResponse;
import com.springboot.app.accounts.entity.User;
import com.springboot.app.dto.response.ServiceResponse;
import org.springframework.stereotype.Component;

@Component
public interface PersonService {
    ServiceResponse<Void> updatePersonalInfo(User user, AccountInfo accountInfo);

    ServiceResponse<String> getAvatarByUsername(String username);

    ServiceResponse<AccountInfoResponse> getUserInfoByUsername(String username);


}
