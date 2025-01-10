package com.springboot.app.emails.service;

import com.springboot.app.dto.response.AckCodeType;
import com.springboot.app.dto.response.ServiceResponse;
import com.springboot.app.emails.dto.request.PassResetEmailRequest;
import com.springboot.app.emails.dto.request.RegistationEmailRequest;
import com.springboot.app.emails.entity.RegistrationOption;
import com.springboot.app.emails.repository.RegistrationOptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RegistrationOptionServiceImpl implements RegistrationOptionService {
    @Autowired
    private RegistrationOptionRepository registrationOptionRepository;


    public ServiceResponse<RegistrationOption> getRegistrationOptionById(Long id) {
        ServiceResponse<RegistrationOption> response = new ServiceResponse<>();
        RegistrationOption registrationOption = registrationOptionRepository.findById(id).orElse(null);
        if (registrationOption == null) {
            response.setAckCode(AckCodeType.FAILURE);
            response.addMessage("Registration option not found");
            return response;
        }
        response.setDataObject(registrationOption);
        return response;
    }


    public ServiceResponse<Void> updatePassResetEmailOption(PassResetEmailRequest option) {
        ServiceResponse<Void> response = new ServiceResponse<>();
        Long id = 1L;
        RegistrationOption registrationOption = registrationOptionRepository.findById(id).orElse(null);
        if (registrationOption == null) {
            response.addMessage("Registration option not found");
            return response;
        }
        List<String> errors = validateEmailOption(option);
        if (!errors.isEmpty()) {
            response.setAckCode(AckCodeType.FAILURE);
            response.addMessages(errors);
            return response;
        }
        registrationOption.setPasswordResetEmailSubject(option.getSubject());
        registrationOption.setPasswordResetEmailTemplate(option.getTemplate());
        registrationOptionRepository.save(registrationOption);
        response.addMessage("Update registration option successfully");
        return response;
    }

    public ServiceResponse<Void> updateRegistrationEmailOption(RegistationEmailRequest option) {
        ServiceResponse<Void> response = new ServiceResponse<>();
        Long id = 1L;
        RegistrationOption registrationOption = registrationOptionRepository.findById(id).orElse(null);
        if (registrationOption == null) {
            response.addMessage("Registration option not found");
            return response;
        }
        List<String> errors = validateEmailOption(option);
        if (!errors.isEmpty()) {
            response.setAckCode(AckCodeType.FAILURE);
            response.addMessages(errors);
            return response;
        }
        registrationOption.setRegistrationEmailSubject(option.getSubject());
        registrationOption.setRegistrationEmailTemplate(option.getTemplate());
        registrationOptionRepository.save(registrationOption);
        response.addMessage("Update registration option successfully");
        return response;
    }


    private List<String> validateEmailOption(Object option) {
        List<String> errors = new ArrayList<>();
        if (option == null) {
            errors.add("Option is null");
        }
        if (option instanceof PassResetEmailRequest passResetEmailRequest) {
            if (passResetEmailRequest.getSubject() == null) {
                errors.add("Subject is null");
            }
            if (passResetEmailRequest.getTemplate() == null) {
                errors.add("Template is null");
            }
        }
        if (option instanceof RegistationEmailRequest registationEmailRequest) {
            if (registationEmailRequest.getSubject() == null) {
                errors.add("Subject is null");
            }
            if (registationEmailRequest.getTemplate() == null) {
                errors.add("Template is null");
            }
        }
        return errors;

    }
}
