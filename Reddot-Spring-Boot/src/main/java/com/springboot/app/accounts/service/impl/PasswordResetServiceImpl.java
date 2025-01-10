package com.springboot.app.accounts.service.impl;

import com.springboot.app.accounts.dto.request.PasswordRequest;
import com.springboot.app.accounts.entity.PasswordReset;
import com.springboot.app.accounts.entity.User;
import com.springboot.app.accounts.repository.PasswordResetRepository;
import com.springboot.app.accounts.repository.UserRepository;
import com.springboot.app.accounts.service.PasswordResetService;
import com.springboot.app.dto.response.AckCodeType;
import com.springboot.app.dto.response.ServiceResponse;
import com.springboot.app.emails.EmailSender;
import com.springboot.app.emails.entity.EmailOption;
import com.springboot.app.emails.entity.RegistrationOption;
import com.springboot.app.emails.repository.EmailOptionRepository;
import com.springboot.app.emails.repository.RegistrationOptionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionInterceptor;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@Service
public class PasswordResetServiceImpl implements PasswordResetService {

    private static final Logger logger = LoggerFactory.getLogger(PasswordResetServiceImpl.class);
    private static final String REDIRECT_URL = "http://localhost:5173/reset-password?key=";
    private static final String APP_NAME = "Tech Forum";
    @Autowired
    PasswordEncoder encoder;
    @Autowired
    private PasswordResetRepository passwordResetRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EmailOptionRepository emailOptionRepository;
    @Autowired
    private RegistrationOptionRepository registrationOptionRepository;
    @Value("${Scheduled.cleanPasswordReset.timePassed.minutes}")
    private int timePassedMinutes;

    @Transactional(readOnly = false)
    public ServiceResponse<Void> sendPasswordResetEmail(String email) {
        ServiceResponse<Void> response = new ServiceResponse<>();
        // check if passwordReset already exists for the given email
        if (passwordResetExists(email)) {
            response.setAckCode(AckCodeType.FAILURE);
            response.addMessage("Password reset exists");
        } else if (Boolean.TRUE.equals(userRepository.existsByEmail(email))) {
            PasswordReset passwordReset = new PasswordReset();
            passwordReset.setEmail(email);
            passwordReset.setResetKey(UUID.randomUUID().toString());
            // save passwordReset
            passwordResetRepository.save(passwordReset);
            logger.info("Password reset request for email {}", email);

            try {
                emailPasswordReset(passwordReset);
            } catch (Exception e) {
                logger.error("Error sending password reset email", e);
                response.setAckCode(AckCodeType.FAILURE);
                response.addMessage("Error sending password reset email");
                TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
                return response;
            }
            response.setAckCode(AckCodeType.SUCCESS);
            response.addMessage("Sent password reset email successfully.");
        } else {
            response.setAckCode(AckCodeType.FAILURE);
            response.addMessage("email.not.found");
        }
        return response;
    }

    private boolean passwordResetExists(String email) {
        return passwordResetRepository.existsByEmail(email);
    }

    /**
     * Helper method to send password reset email
     *
     * @param passwordReset
     */
    public void emailPasswordReset(PasswordReset passwordReset) throws Exception {
        Long id = 1L;
        RegistrationOption registrationOption = registrationOptionRepository.findById(id).get();
        EmailOption emailOption = emailOptionRepository.findById(id).get();

        EmailSender emailSender = EmailSender.builder()
                .host(emailOption.getHost())
                .port(emailOption.getPort())
                .username(emailOption.getUsername())
                .password(emailOption.getPassword())
                .tlsEnabled(emailOption.getTlsEnable())
                .defaultEncoding("UTF-8").authentication(emailOption.getAuthentication()).build();

        emailSender.sendEmail(emailOption.getUsername(), passwordReset.getEmail(),
                registrationOption.getPasswordResetEmailSubject(),
                buildPasswordResetEmailContent(registrationOption.getPasswordResetEmailTemplate(), passwordReset),
                true
        );
    }

    /*
     * replace the following patterns: #username, #email, and #confirm-url with values from registration and system
     */
    public String buildPasswordResetEmailContent(String emailTemplate, PasswordReset passwordReset) {
        User user = userRepository.findByEmail(passwordReset.getEmail()).get();
        String username = user.getName() != null ? user.getName() : user.getUsername();
        emailTemplate = emailTemplate.replaceAll("#username", username)
                .replaceAll("#email", passwordReset.getEmail())
                .replaceAll("#reset-url",
                        "<a href=\"" + REDIRECT_URL + passwordReset.getResetKey()
                        + "\">" + APP_NAME + "</a>");
        return emailTemplate;
    }

    @Scheduled(fixedRateString = "${Scheduled.cleanPasswordReset.interval.miliseconds}",
            initialDelayString = "${Scheduled.cleanPasswordReset.initialDelay.miliseconds}")
    @Transactional(readOnly = false)
    public void cleanPasswordRest() {
        logger.info("Cleaning up password reset requests older than {} minutes", timePassedMinutes);
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MINUTE, -timePassedMinutes);
        Date thresholdDate = cal.getTime();
        Integer deletedCount = passwordResetRepository.deleteLessThan(thresholdDate);
        logger.info("Deleted {} password reset requests", deletedCount);
    }


    @Transactional(readOnly = false)
    public ServiceResponse<Void> updatePassword(PasswordRequest passwordRequest) {
        ServiceResponse<Void> response = new ServiceResponse<>();
        if (passwordRequest.getKey() == null || passwordRequest.getKey().isEmpty()) {
            response.setAckCode(AckCodeType.FAILURE);
            response.addMessage("Error: Password reset key is required!");
            return response;
        }
        if (!passwordRequest.getNewPassword().matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%]).{8,24}$")) {
            response.setAckCode(AckCodeType.FAILURE);
            response.addMessage("Error: Password must contain at least 8 characters, 1 uppercase letter, 1 lowercase letter, 1 number and 1 special character!");
            return response;
        }

        LocalDateTime now = LocalDateTime.now();
        PasswordReset passwordReset = passwordResetRepository.findByResetKey(passwordRequest.getKey()).orElse(null);
        if (passwordReset == null) {
            response.setAckCode(AckCodeType.FAILURE);
            response.addMessage("Password reset invalid key");
            return response;
        } else if (passwordReset.getCreatedAt().plusMinutes(timePassedMinutes).isBefore(now)) {
            response.setAckCode(AckCodeType.FAILURE);
            response.addMessage("Password reset expired");
            passwordResetRepository.delete(passwordReset); // delete expired password reset
            return response;
        }

        User user = userRepository.findByEmail(passwordReset.getEmail()).get();
        user.setPassword(encoder.encode(passwordRequest.getNewPassword()));
        userRepository.save(user);
        passwordResetRepository.delete(passwordReset);
        response.setAckCode(AckCodeType.SUCCESS);
        response.addMessage("Password reset successfully!");

        return response;
    }
}
