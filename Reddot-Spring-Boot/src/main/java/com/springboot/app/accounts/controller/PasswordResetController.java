package com.springboot.app.accounts.controller;

import com.springboot.app.accounts.dto.request.PasswordRequest;
import com.springboot.app.accounts.repository.DeletedUserRepository;
import com.springboot.app.accounts.repository.UserRepository;
import com.springboot.app.accounts.service.PasswordResetService;
import com.springboot.app.dto.response.AckCodeType;
import com.springboot.app.dto.response.ObjectResponse;
import com.springboot.app.dto.response.ServiceResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reset-password")
public class PasswordResetController {

    private static final Logger logger = LoggerFactory.getLogger(PasswordResetController.class);

    @Autowired
    private PasswordResetService passwordResetService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DeletedUserRepository deletedUserRepository;

    @PostMapping("/request")
    public ResponseEntity<ObjectResponse> requestPasswordReset(@RequestParam("email") String email) {
        logger.info("Reset password request for email {}", email);
        boolean user = userRepository.existsByEmail(email);
        if (!user) {
            return ResponseEntity
                    .badRequest()
                    .body(new ObjectResponse("400", "Error: Email not found!", null));
        }
        boolean deleteUser = deletedUserRepository.existsByEmail(email);
        if (deleteUser) {
            return ResponseEntity
                    .badRequest()
                    .body(new ObjectResponse("400", "Please enter email another!", null));
        }

        ServiceResponse<Void> serviceResponse = passwordResetService.sendPasswordResetEmail(email);
        if (serviceResponse.getAckCode() != AckCodeType.SUCCESS) {
            return ResponseEntity
                    .badRequest()
                    .body(new ObjectResponse("400", "Error: Password reset link could not be sent!", null));
        }
        return ResponseEntity.ok(new ObjectResponse("200", "Password reset link sent to your email!", null));
    }

    @PostMapping("/reset")
    public ResponseEntity<ObjectResponse> resetPassword(@Valid @RequestBody PasswordRequest passwordRequest) {
        ServiceResponse<Void> response = passwordResetService.updatePassword(passwordRequest);
        if (response.getAckCode() != AckCodeType.SUCCESS) {
            return ResponseEntity
                    .badRequest()
                    .body(new ObjectResponse("400", response.getMessages().getFirst(), null));
        }
        return ResponseEntity.ok(new ObjectResponse("200", "Password reset successfully!", null));
    }
}
