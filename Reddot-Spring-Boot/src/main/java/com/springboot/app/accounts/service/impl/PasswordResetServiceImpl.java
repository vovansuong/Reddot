package com.springboot.app.accounts.service.impl;

import com.springboot.app.accounts.dto.request.PasswordRequest;
import com.springboot.app.accounts.entity.RecoveryToken;
import com.springboot.app.accounts.entity.User;
import com.springboot.app.accounts.repository.RecoveryTokenRepository;
import com.springboot.app.accounts.repository.UserRepository;
import com.springboot.app.accounts.service.PasswordResetService;
import com.springboot.app.dto.response.AckCodeType;
import com.springboot.app.dto.response.ServiceResponse;
import com.springboot.app.email.entity.EmailContentOption;
import com.springboot.app.email.entity.EmailOptionType;
import com.springboot.app.email.service.EmailOptionService;
import com.springboot.app.exception.ResourceNotFoundException;
import com.springboot.app.service.email.MailSenderManager;
import com.springboot.app.utils.ResourceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionInterceptor;

import java.time.LocalDateTime;

@Service
public class PasswordResetServiceImpl implements PasswordResetService {

    private static final Logger logger = LoggerFactory.getLogger(PasswordResetServiceImpl.class);
    private static final String REDIRECT_URL = "http://localhost:5173/reset-password?key=";
    @Autowired
    PasswordEncoder encoder;
    @Autowired
    private RecoveryTokenRepository recoveryTokenRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MailSenderManager mailSenderManager;
    @Autowired
    private EmailOptionService emailOptionService;

    @Transactional
    public ServiceResponse<Void> sendPasswordResetEmail(String email) {
        ServiceResponse<Void> response = new ServiceResponse<>();
        // check if passwordReset already exists for the given email
        if (passwordResetExists(email)) {
            response.setAckCode(AckCodeType.FAILURE);
            response.addMessage("Password reset exists");
        } else if (userRepository.existsByEmail(email)) {
            User user = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
            RecoveryToken recoveryToken = new RecoveryToken(user.getId());
            recoveryToken.setEmail(email);
            recoveryTokenRepository.save(recoveryToken);
            logger.info("Password reset request for email {}", email);
            try {
                emailPasswordReset(recoveryToken);
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
        return recoveryTokenRepository.existsByEmail(email);
    }

    /**
     * Helper method to send password reset email
     */
    public void emailPasswordReset(RecoveryToken recoveryToken) {
        User user = userRepository.findByEmail(recoveryToken.getEmail()).orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + recoveryToken.getEmail()));
        EmailContentOption contentOption = emailOptionService.getEmailOptionByType(EmailOptionType.PASSWORD_RESET);
        ResourceUtil.ResourcePartObject object = new ResourceUtil.ResourcePartObject(user.getUsername(), user.getEmail(), null, REDIRECT_URL + recoveryToken.getResetKey());
        String msg = ResourceUtil.buildEmailContentFromTemplate(contentOption.getEmailBodyTemplate(), object);
        mailSenderManager.sendEmail(
                recoveryToken.getEmail(),
                contentOption.getEmailSubject(), msg, true);
    }

    @Transactional()
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
        RecoveryToken recoveryToken = recoveryTokenRepository.findByResetKey(passwordRequest.getKey()).orElse(null);
        if (recoveryToken == null) {
            response.setAckCode(AckCodeType.FAILURE);
            response.addMessage("Password reset invalid key");
            return response;
        } else if (recoveryToken.getValidBefore().isBefore(now)) {
            response.setAckCode(AckCodeType.FAILURE);
            response.addMessage("Password reset expired");
            recoveryTokenRepository.delete(recoveryToken); // delete expired password reset
            return response;
        } else if (recoveryToken.isUsed()) {
            response.setAckCode(AckCodeType.FAILURE);
            response.addMessage("Password reset key already used");
            recoveryTokenRepository.delete(recoveryToken); // delete used password reset
            return response;
        }

        User user = userRepository.findByEmail(recoveryToken.getEmail()).orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + recoveryToken.getEmail()));
        user.setPassword(encoder.encode(passwordRequest.getNewPassword()));
        userRepository.save(user);
        recoveryTokenRepository.delete(recoveryToken);
        response.setAckCode(AckCodeType.SUCCESS);
        response.addMessage("Password reset successfully!");

        return response;
    }
}
