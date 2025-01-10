package com.springboot.app.emails.service;

import com.springboot.app.dto.response.ServiceResponse;
import com.springboot.app.emails.dto.request.DataEmailRequest;
import com.springboot.app.emails.dto.response.DataEmailResponse;
import com.springboot.app.emails.entity.EmailOption;

import java.util.List;

public interface EmailOptionsService {
    ServiceResponse<EmailOption> getEmailOptionById(Long id);

    ServiceResponse<EmailOption> saveEmailOption(EmailOption emailOption);

    ServiceResponse<Void> updateEmailOption(Long id, EmailOption emailOption);

    ServiceResponse<Void> sendDataEmail(DataEmailRequest dataEmailRequest);

    ServiceResponse<List<DataEmailResponse>> getAllEmail();

    ServiceResponse<Void> sendNotificationChangePassword(String name, String email);
}
