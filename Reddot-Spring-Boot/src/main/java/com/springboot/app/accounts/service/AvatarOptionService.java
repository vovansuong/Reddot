package com.springboot.app.accounts.service;

import com.springboot.app.accounts.dto.request.AvatarOptionRequest;
import com.springboot.app.accounts.entity.AvatarOption;
import com.springboot.app.dto.response.ServiceResponse;
import org.springframework.stereotype.Component;

@Component
public interface AvatarOptionService {
    AvatarOption getAvatarOption();

    ServiceResponse<Void> updateAvatarOption(AvatarOptionRequest option);
}
