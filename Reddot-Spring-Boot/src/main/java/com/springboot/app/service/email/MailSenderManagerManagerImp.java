package com.springboot.app.service.email;

import jakarta.mail.Message;
import jakarta.mail.internet.InternetAddress;
import lombok.Setter;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

import java.util.Set;

@Setter
@Service
public class MailSenderManagerManagerImp implements MailSenderManager {


    private final JavaMailSenderImpl mailSender;

    public MailSenderManagerManagerImp(JavaMailSenderImpl mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendEmail(String to, String subject, String body, boolean asHtml) {
        MimeMessagePreparator preparation = mimeMessage -> {
            mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
            mimeMessage.setFrom(new InternetAddress("Reddot"));
            mimeMessage.setSubject(subject);
            mimeMessage.setContent(body, "text/html;charset=UTF-8");
        };

        this.send(preparation);
    }

    @Override
    public void sendEmails(Set<String> emails, String subject, String body, boolean asHtml) {
        emails.forEach(email -> sendEmail(email, subject, body, asHtml));
    }

    private void send(MimeMessagePreparator preparation) {
        try {
            mailSender.send(preparation);
        } catch (MailException e) {
            throw new MailSendException("Failed to send email", e);
        }
    }
}
