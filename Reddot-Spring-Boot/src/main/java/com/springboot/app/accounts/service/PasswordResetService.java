package com.springboot.app.accounts.service;


import com.springboot.app.accounts.dto.request.PasswordRequest;
import com.springboot.app.dto.response.ServiceResponse;
import org.springframework.stereotype.Service;

@Service
public interface PasswordResetService {
    ServiceResponse<Void> sendPasswordResetEmail(String email);

    ServiceResponse<Void> updatePassword(PasswordRequest passwordRequest);
}
