package com.springboot.app.accounts.dto.request;

import lombok.Data;

@Data
public class PasswordRequest {
    private String key;
    private String newPassword;
}
