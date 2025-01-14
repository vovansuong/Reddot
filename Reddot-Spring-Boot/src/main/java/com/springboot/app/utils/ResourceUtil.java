package com.springboot.app.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Service
public class ResourceUtil {

    public static String getRegistrationTemplate() {
        return """
                <!DOCTYPE html>
                <html lang="en">
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <title>Confirm Your Registration</title>
                </head>
                <body style="font-family: Arial, sans-serif; margin: 0; padding: 0; background-color: #f9f9f9;">
                    <div style="max-width: 600px; margin: 20px auto; background: #ffffff; padding: 20px; border: 1px solid #ddd; border-radius: 8px;">
                        <h2 style="text-align: center; color: #333;">Welcome to Reddot</h2>
                        <p>Hi #username,</p>
                <p>Thank you for registering! Please click the button below to confirm your email address: #email</p>
                <p style="text-align: center;">
                    <a href="#url" style="background: #007BFF; color: #ffffff; text-decoration: none; padding: 10px 20px; border-radius: 5px;">Confirm Email</a>
                        </p>
                        <p>If you did not sign up, please ignore this email.</p>
                    </div>
                </body>
                </html>
                """;
    }

    public static String getPasswordForgotTemplate() {
        return """
                <!DOCTYPE html>
                <html lang="en">
                <head>
                    <meta charset="UTF-8">
                    <meta content="width=device-width, initial-scale=1.0" name="viewport">
                    <title>Reset Your Password</title>
                </head>
                <body style="font-family: Arial, sans-serif; margin: 0; padding: 0; background-color: #f9f9f9;">
                <div style="max-width: 600px; margin: 20px auto; background: #ffffff; padding: 20px; border: 1px solid #ddd; border-radius: 8px;">
                    <h2 style="text-align: center; color: #333;">Password Reset</h2>
                    <p>Hi #username,</p>
                    <p>You recently requested to reset your password. Please click the button below to reset your password:</p>
                    <p style="text-align: center;">
                        <a href="#url"
                           style="background: #007BFF; color: #ffffff; text-decoration: none; padding: 10px 20px; border-radius: 5px;">Reset
                            Password</a>
                    </p>
                    <p>If you did not request a password reset, please ignore this email.</p>
                </div>
                </body>
                </html>
                """;
    }

    public static String getSendResetPasswordTemplate() {
        return """
                <!DOCTYPE html>
                <html lang="en">
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <title>Request Reset Password</title>
                </head>
                <body style="font-family: Arial, sans-serif; margin: 0; padding: 0; background-color: #f9f9f9;">
                    <div style="max-width: 600px; margin: 20px auto; background: #ffffff; padding: 20px; border: 1px solid #ddd; border-radius: 8px;">
                        <h2 style="text-align: center; color: #333;">Password Reset</h2>
                        <p>Hi #username,</p>
                <p>You recently requested to reset your password. Please click the button below to reset your password:</p>
                <p style="text-align: center;">
                    <a href="#url" style="background: #007BFF; color: #ffffff; text-decoration: none; padding: 10px 20px; border-radius: 5px;">Reset Password</a>
                        </p>
                        <p>If you did not request a password reset, please ignore this email.</p>
                    </div>
                </body>
                </html>
                """;
    }


    public static String getOAuthRegistrationNotificationTemplate() {
        return """
                <!DOCTYPE html>
                <html lang="en">
                <head>
                    <meta charset="UTF-8">
                    <meta content="width=device-width, initial-scale=1.0" name="viewport">
                    <title>New Account Registration</title>
                </head>
                <body style="font-family: Arial, sans-serif; margin: 0; padding: 0; background-color: #f9f9f9;">
                <div style="max-width: 600px; margin: 20px auto; background: #ffffff; padding: 20px; border: 1px solid #ddd; border-radius: 8px;">
                    <h2 style="text-align: center; color: #333;">Welcome to Reddot</h2>
                    <p>Hi #username,</p>
                    <p>You have successfully created an Reddot account using OAuth2 account.</p>
                    <p style="text-align: center;">
                        <b>Your default password is: #password</b>
                    </p>
                    <b style="color: red">Important:</b>
                    <p>For security reasons, please change your password after logging in.</p>
                </div>
                </body>
                </html>
                """;
    }

    public static String buildEmailContentFromTemplate(String emailTemplate, ResourcePartObject o) {
        if (emailTemplate == null || o == null) {
            throw new IllegalArgumentException("Email template and resource part object must not be null");
        }

        String result = emailTemplate;
        if (result.contains("#username")) {
            result = result.replace("#username", o.getUsername() != null ? o.getUsername() : "");
        }
        if (result.contains("#email")) {
            result = result.replace("#email", o.getEmail() != null ? o.getEmail() : "");
        }
        if (result.contains("#password")) {
            result = result.replace("#password", o.getPassword() != null ? o.getPassword() : "");
        }
        if (result.contains("#url")) {
            result = result.replace("#url", o.getUrl() != null ? o.getUrl() : "");
        }

        return result;
    }

    @Setter
    @Getter
    @AllArgsConstructor
    public static class ResourcePartObject {

        private String username;
        private String email;
        private String password;
        private String url;
    }
}
