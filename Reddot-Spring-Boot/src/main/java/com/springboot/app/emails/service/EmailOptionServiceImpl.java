package com.springboot.app.emails.service;

import com.springboot.app.accounts.entity.User;
import com.springboot.app.accounts.repository.UserRepository;
import com.springboot.app.dto.response.AckCodeType;
import com.springboot.app.dto.response.ServiceResponse;
import com.springboot.app.emails.EmailSender;
import com.springboot.app.emails.dto.request.DataEmailRequest;
import com.springboot.app.emails.dto.response.DataEmailResponse;
import com.springboot.app.emails.entity.EmailOption;
import com.springboot.app.emails.repository.EmailOptionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionInterceptor;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class EmailOptionServiceImpl implements EmailOptionsService {
    private static final Logger logger = LoggerFactory.getLogger(EmailOptionServiceImpl.class);

    @Autowired
    private EmailOptionRepository emailOptionRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public ServiceResponse<EmailOption> getEmailOptionById(Long id) {
        ServiceResponse<EmailOption> response = new ServiceResponse<>();
        EmailOption emailOption = emailOptionRepository.findById(id).orElse(null);
        if (emailOption == null) {
            response.setAckCode(AckCodeType.FAILURE);
            response.addMessage("Email option not found");
            return response;
        }
        response.setDataObject(emailOption);
        return response;
    }

    @Override
    public ServiceResponse<EmailOption> saveEmailOption(EmailOption emailOption) {
        ServiceResponse<EmailOption> response = new ServiceResponse<>();

        List<String> errors = validateEmailOption(emailOption);
        if (!errors.isEmpty()) {
            response.setAckCode(AckCodeType.FAILURE);
            response.addMessages(errors);
            return response;
        }

        EmailOption savedEmailOption = emailOptionRepository.save(emailOption);
        response.setDataObject(savedEmailOption);
        return response;
    }

    @Override
    public ServiceResponse<Void> updateEmailOption(Long id, EmailOption emailOption) {
        ServiceResponse<Void> response = new ServiceResponse<>();

        List<String> errors = validateEmailOption(emailOption);
        if (!errors.isEmpty()) {
            response.setAckCode(AckCodeType.FAILURE);
            response.addMessages(errors);
            return response;
        }

        EmailOption existingEmailOption = emailOptionRepository.findById(id).orElse(null);
        if (existingEmailOption == null) {
            response.setAckCode(AckCodeType.FAILURE);
            response.addMessage("Email option not found");
            return response;
        }
        existingEmailOption.setHost(emailOption.getHost());
        existingEmailOption.setPort(emailOption.getPort());
        existingEmailOption.setUsername(emailOption.getUsername());
        existingEmailOption.setPassword(emailOption.getPassword());
        existingEmailOption.setTlsEnable(emailOption.getTlsEnable());
        existingEmailOption.setAuthentication(emailOption.getAuthentication());
        emailOptionRepository.save(existingEmailOption);
        return response;
    }


    private List<String> validateEmailOption(EmailOption emailOption) {
        List<String> errors = new ArrayList<>();
        // validate email option
        if (emailOption.getHost() == null || emailOption.getHost().isEmpty()) {
            errors.add("Host is required");
        }
        if (emailOption.getPort() == null || emailOption.getPort() == 0) {
            errors.add("Port is required");
        }
        if (emailOption.getUsername() == null || emailOption.getUsername().isEmpty()) {
            errors.add("Username is required");
        }
        if (emailOption.getPassword() == null || emailOption.getPassword().isEmpty()) {
            errors.add("Password is required");
        }
        if (emailOption.getTlsEnable() == null) {
            errors.add("TlsEnable is required");
        }
        if (emailOption.getAuthentication() == null) {
            errors.add("Authentication is required");
        }
        return errors;
    }

    @Override
    public ServiceResponse<Void> sendDataEmail(DataEmailRequest dataEmailRequest) {
        ServiceResponse<Void> response = new ServiceResponse<>();

        List<String> errors = validateDataEmailRequest(dataEmailRequest);
        if (!errors.isEmpty()) {
            response.setAckCode(AckCodeType.FAILURE);
            response.addMessages(errors);
            return response;
        }
        try {
            // send email
            emailData(dataEmailRequest);


        } catch (Exception e) {
            logger.error("Error sending email", e);
            response.setAckCode(AckCodeType.FAILURE);
            response.addMessage("Error sending email");
            TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
            return response;
        }

        return response;
    }

    private void emailData(DataEmailRequest dataEmailRequest) throws Exception {
        Long id = 1L;
        EmailOption emailOption = emailOptionRepository.findById(id).get();

        EmailSender emailSender = EmailSender.builder()
                .host(emailOption.getHost())
                .port(emailOption.getPort())
                .username(emailOption.getUsername())
                .password(emailOption.getPassword())
                .tlsEnabled(emailOption.getTlsEnable())
                .defaultEncoding("UTF-8").authentication(emailOption.getAuthentication()).build();

        String[] toEmails = dataEmailRequest.getEmails().toArray(new String[0]);
        emailSender.sendEmailToList(emailOption.getUsername(), toEmails,
                dataEmailRequest.getSubject(),
                dataEmailRequest.getTemplate(),
                true
        );
    }

    private List<String> validateDataEmailRequest(DataEmailRequest dataEmailRequest) {
        List<String> errors = new ArrayList<>();
        if (dataEmailRequest == null) {
            errors.add("DataEmailRequest is null");
        }
        if (dataEmailRequest.getEmails().isEmpty()) {
            errors.add("To is required");
        }
        if (dataEmailRequest.getSubject() == null || dataEmailRequest.getSubject().isEmpty()) {
            errors.add("Subject is required");
        }
        if (dataEmailRequest.getTemplate() == null || dataEmailRequest.getTemplate().isEmpty()) {
            errors.add("Content is required");
        }
        return errors;
    }

    public ServiceResponse<List<DataEmailResponse>> getAllEmail() {
        ServiceResponse<List<DataEmailResponse>> response = new ServiceResponse<>();
        List<User> users = userRepository.findAll();
        List<DataEmailResponse> dataEmailResponses = users.stream().map(this::mapUserToDataEmailResponse).collect(Collectors.toList());
        response.setDataObject(dataEmailResponses);
        return response;
    }

    private DataEmailResponse mapUserToDataEmailResponse(User user) {
        DataEmailResponse dataEmailResponse = new DataEmailResponse();
        dataEmailResponse.setUsername(user.getUsername());
        dataEmailResponse.setEmail(user.getEmail());
        return dataEmailResponse;
    }

    //Another method to send email

    public ServiceResponse<Void> sendNotificationChangePassword(String name, String email) {
        DataEmailRequest dataEmailRequest = new DataEmailRequest();
        dataEmailRequest.setEmails(Set.of(email));
        dataEmailRequest.setSubject("Notification");
        //change password
        String template = "<h4>Hello " + name + ",</h4>\n\n" +
                          "Your password has been changed.\n\n" +
                          "Now you can login with your new password.\n\n" +
                          "Thanks"
                          + "\n\n" + "<h4>TechForums team<h4>";
        dataEmailRequest.setTemplate(template);
        return sendDataEmail(dataEmailRequest);
    }


    public ServiceResponse<Void> sendNotificationChangeInfo(String name, String email) {
        DataEmailRequest dataEmailRequest = new DataEmailRequest();
        dataEmailRequest.setEmails(Set.of(email));
        dataEmailRequest.setSubject("Notification");
        //change password
        String template = "<h4>Hello " + name + ",</h4>\n\n" +
                          "Your info account has been updated.\n\n" +
                          "Now you can login with your new info.\n\n" +
                          "Thanks"
                          + "\n\n" + "<h4>TechForums team<h4>";
        dataEmailRequest.setTemplate(template);
        return sendDataEmail(dataEmailRequest);
    }


}
