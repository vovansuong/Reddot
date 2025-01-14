package com.springboot.app.email.service;

import com.springboot.app.email.entity.EmailContentOption;
import com.springboot.app.email.entity.EmailOptionType;
import com.springboot.app.exception.ResourceNotFoundException;
import org.springframework.stereotype.Component;

@Component
public interface EmailOptionService {
    EmailContentOption getEmailOptionByType(EmailOptionType optionType) throws ResourceNotFoundException;
}
