package com.springboot.app.accounts.service.impl;

import com.springboot.app.accounts.dto.request.AvatarOptionRequest;
import com.springboot.app.accounts.entity.AvatarOption;
import com.springboot.app.accounts.repository.AvatarOptionRepository;
import com.springboot.app.accounts.service.AvatarOptionService;
import com.springboot.app.dto.response.AckCodeType;
import com.springboot.app.dto.response.ServiceResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AvatarOptionServiceImpl implements AvatarOptionService {
    private static final Logger logger = LoggerFactory.getLogger(AvatarOptionServiceImpl.class);

    @Autowired
    private AvatarOptionRepository avatarOptionRepository;

    @Override
    public AvatarOption getAvatarOption() {
        logger.info("get avatar option");
        Long id = 1L;
        return avatarOptionRepository.findById(id).orElse(null);
    }

    public ServiceResponse<Void> updateAvatarOption(AvatarOptionRequest option) {
        logger.info("update avatar option {}, {}, {}",
                option.getMaxFileSize(), option.getMaxHeight(), option.getMaxWidth());
        logger.info("update avatar option");
        ServiceResponse<Void> response = new ServiceResponse<>();
        List<String> errors = validateAvatarOption(option);
        if (!errors.isEmpty()) {
            response.setMessages(errors);
            response.setAckCode(AckCodeType.FAILURE);
            return response;
        }
        AvatarOption avatarOption = avatarOptionRepository.findById(1L).orElse(new AvatarOption());
        avatarOption.setMaxFileSize(option.getMaxFileSize());
        avatarOption.setMaxHeight(option.getMaxHeight());
        avatarOption.setMaxWidth(option.getMaxWidth());

        avatarOptionRepository.save(avatarOption);
        return response;
    }

    private List<String> validateAvatarOption(AvatarOptionRequest option) {
        List<String> errors = new ArrayList<String>(); //
        if (option.getMaxFileSize() < 100 || option.getMaxFileSize() > 1000000) {
            errors.add("Max file size must be between 100 and 1000000");
        }
        if (option.getMaxHeight() < 100 || option.getMaxHeight() > 1200) {
            errors.add("Max height must be between 100 and 1200");
        }
        if (option.getMaxWidth() < 100 || option.getMaxWidth() > 1200) {
            errors.add("Max width must be between 100 and 1200");
        }
        return errors;
    }


}
