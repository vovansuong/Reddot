package com.springboot.app.email.service;

import com.springboot.app.email.entity.EmailContentOption;
import com.springboot.app.email.entity.EmailOptionType;
import com.springboot.app.email.repository.EmailContentOptionRepository;
import com.springboot.app.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class EmailOptionServiceImp implements EmailOptionService {
    private final EmailContentOptionRepository emailContentOptionRepository;

    public EmailOptionServiceImp(EmailContentOptionRepository emailContentOptionRepository) {
        this.emailContentOptionRepository = emailContentOptionRepository;
    }

    @Override
    public EmailContentOption getEmailOptionByType(EmailOptionType optionType) {
        try {
            return emailContentOptionRepository.findByOptionType(optionType);
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error while getting email subject");
        }
    }
}
