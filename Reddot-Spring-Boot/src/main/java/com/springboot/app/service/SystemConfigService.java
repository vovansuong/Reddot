package com.springboot.app.service;

import com.springboot.app.accounts.entity.AvatarOption;
import com.springboot.app.accounts.repository.AvatarOptionRepository;
import com.springboot.app.dto.response.ServiceResponse;
import com.springboot.app.forums.entity.CommentOption;
import com.springboot.app.repository.GenericDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Service
@Transactional
public class SystemConfigService {
    @Autowired
    private GenericDAO genericDAO;

    @Autowired
    private AvatarOptionRepository avatarOptionRepository;

    public ServiceResponse<AvatarOption> getAvatarOption() {
        ServiceResponse<AvatarOption> response = new ServiceResponse<>();
//		AvatarOption avatarOption = avatarOptionRepository.getReferenceById(1L);
        AvatarOption avatarOption = genericDAO.getEntity(AvatarOption.class, Collections.singletonMap("id", 1L));
        response.setDataObject(avatarOption);
        return response;
    }

    public ServiceResponse<AvatarOption> updateAvatarOption(AvatarOption avatarOption) {
        ServiceResponse<AvatarOption> response = new ServiceResponse<>();
//		avatarOptionRepository.save(avatarOption);
        genericDAO.persist(avatarOption);
        response.setDataObject(avatarOption);
        return response;
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public ServiceResponse<CommentOption> getCommentOption() {
        ServiceResponse<CommentOption> response = new ServiceResponse<>();
        CommentOption commentOption = genericDAO.getEntity(CommentOption.class, Collections.singletonMap("id", 1L));
        response.setDataObject(commentOption);
        return response;
    }


}
