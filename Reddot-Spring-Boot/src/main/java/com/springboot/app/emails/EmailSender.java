package com.springboot.app.emails;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import java.io.File;
import java.util.Arrays;
import java.util.Properties;

public class EmailSender {
    public static final Logger logger = LoggerFactory.getLogger(EmailSender.class);

    private JavaMailSenderImpl mailSender;

    public static Builder builder() {
        return new Builder();
    }

    /**
     * Send email to a single email address
     */
    public void sendEmail(String fromAddress,
                          String toAddress,
                          String subject, String body,
                          boolean sendAsHtml)
            throws Exception {
        send(fromAddress, new String[]{toAddress}, null, null, subject, body, null, null, sendAsHtml);
    }

    /**
     * Send email to multiple email addresses
     */
    public void sendEmailToList(String fromAddress,
                                String[] toAddress,
                                String subject, String body,
                                boolean sendAsHtml)
            throws Exception {
        send(fromAddress, toAddress, null, null, subject, body, null, null, sendAsHtml);
    }

    /**
     * Send email to single email addresses with attachment
     */
    public void sendEmailWithAttrachment(String fromAddress,
                                         String toAddress,
                                         String subject, String body,
                                         File attachment, String attachmentName)
            throws Exception {
        send(fromAddress, new String[]{toAddress}, null, null,
                subject, body,
                attachment != null ? new FileSystemResource(attachment) : null,
                attachmentName, false);
    }

    /**
     * Send email to single email addresses with attachment (byte[])
     */
    public void sendEmailWithAttachmment(String fromAddress,
                                         String toAddress,
                                         String subject, String body,
                                         byte[] attachment, String attachmentName)
            throws Exception {
        send(fromAddress, new String[]{toAddress}, null, null,
                subject, body,
                attachment != null ? new ByteArrayResource(attachment) : null,
                attachmentName, false);
    }

    /**
     * Helper method to send email
     */
    public void send(String fromAddress,
                     String[] toAddress, String[] ccAddress, String[] bccAddress,
                     String subject, String body,
                     InputStreamSource attachment, String attachmentName, boolean sendAsHtml)
            throws Exception {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        prepareMimeTypeHelper(helper, fromAddress, toAddress, ccAddress, bccAddress, subject, body, sendAsHtml);
        if (attachment != null && attachmentName != null) {
            helper.addAttachment(attachmentName, attachment);
        }

        mailSender.send(message);
    }

    /**
     * Prepare MimeMessageHelper
     */
    private void prepareMimeTypeHelper(MimeMessageHelper helper,
                                       String fromAddress,
                                       String[] toAddress, String[] ccAddress, String[] bccAddress,
                                       String subject, String body,
                                       boolean sendAsHtml)
            throws MessagingException {
        helper.setFrom(fromAddress);
        toAddress = toAddress == null ? new String[0] : removeEmptyElements(toAddress);
        ccAddress = ccAddress == null ? new String[0] : removeEmptyElements(ccAddress);
        bccAddress = bccAddress == null ? new String[0] : removeEmptyElements(bccAddress);

        if (toAddress.length > 0) {
            helper.setTo(toAddress);
        } else {
            throw new MessagingException("At least one to address must be specified");
        }
        if (ccAddress.length > 0) {
            helper.setCc(ccAddress);
        }
        if (bccAddress.length > 0) {
            helper.setBcc(bccAddress);
        }
        helper.setSubject(subject);
        helper.setText(body, sendAsHtml);
    }

    public String[] removeEmptyElements(String[] array) {
        return Arrays.stream(array)
                .filter(s -> (s != null && !s.isEmpty()))
                .toArray(String[]::new);
    }

    public static class Builder {
        private final EmailSender emailSender;
        private String host;
        private Integer port;
        private String username;
        private String password;
        private String defaultEncoding;
        private Boolean authentication;
        private Boolean tlsEnabled;

        private Builder() {
            this.emailSender = new EmailSender();
        }

        public Builder host(String host) {
            this.host = host;
            return this;
        }

        public Builder port(Integer port) {
            this.port = port;
            return this;
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder defaultEncoding(String defaultEncoding) {
            this.defaultEncoding = defaultEncoding;
            return this;
        }

        public Builder authentication(Boolean authentication) {
            this.authentication = authentication;
            return this;
        }

        public Builder tlsEnabled(Boolean tlsEnabled) {
            this.tlsEnabled = tlsEnabled;
            return this;
        }

        public EmailSender build() {
            emailSender.mailSender = new JavaMailSenderImpl();
            emailSender.mailSender.setHost(host);
            emailSender.mailSender.setPort(port);
            emailSender.mailSender.setUsername(username);
            emailSender.mailSender.setPassword(password);
            emailSender.mailSender.setDefaultEncoding(defaultEncoding);

            Properties props = new Properties();
            props.put("mail.smtp.auth", authentication);
            props.put("mail.smtp.starttls.enable", tlsEnabled);
            emailSender.mailSender.setJavaMailProperties(props);

            return emailSender;
        }
    }


}
