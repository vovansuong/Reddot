package com.springboot.app.accounts.dto.request;


public class PasswordRequest {
    private String key;
    private String newPassword;

    public PasswordRequest() {
    }

    public PasswordRequest(String key, String newPassword) {
        this.key = key;
        this.newPassword = newPassword;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
