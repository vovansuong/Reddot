package com.springboot.app.service.email;

import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public interface MailSenderManager {
    void sendEmail(String email, String subject, String body, boolean asHtml);

    void sendEmails(Set<String> emails, String subject, String body, boolean asHtml);
}