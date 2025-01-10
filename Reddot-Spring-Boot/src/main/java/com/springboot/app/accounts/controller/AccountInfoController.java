package com.springboot.app.accounts.controller;

import com.springboot.app.accounts.dto.request.AccountInfo;
import com.springboot.app.accounts.dto.request.NewPasswordRequest;
import com.springboot.app.accounts.dto.responce.AccountInfoResponse;
import com.springboot.app.accounts.entity.User;
import com.springboot.app.accounts.repository.UserRepository;
import com.springboot.app.accounts.service.PersonService;
import com.springboot.app.accounts.service.StorageService;
import com.springboot.app.accounts.service.UserService;
import com.springboot.app.dto.response.AckCodeType;
import com.springboot.app.dto.response.ObjectResponse;
import com.springboot.app.dto.response.ServiceResponse;
import com.springboot.app.security.jwt.JwtUtils;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/account-info")
public class AccountInfoController {

    private static final Logger logger = LoggerFactory.getLogger(AccountInfoController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private PersonService personService;

    @Autowired
    private StorageService storageService;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/{id}")
    public ResponseEntity<ObjectResponse> getAccountInfo(@PathVariable Long id) {
        User user = userService.findById(id).orElse(null);
        if (user == null) {
            return ResponseEntity.ok(new ObjectResponse("404", "User not found", null));
        }
        return ResponseEntity.ok(new ObjectResponse("200", "Success user", user));
    }

    @GetMapping("/details/{username}")
    public ResponseEntity<ObjectResponse> getAccountInfoByUsername(@PathVariable String username) {
        ServiceResponse<AccountInfoResponse> response = personService.getUserInfoByUsername(username);
        if (response.getAckCode() != AckCodeType.SUCCESS) {
            return ResponseEntity.ok(new ObjectResponse("404", "User not found", null));
        }
        return ResponseEntity.ok(new ObjectResponse("200", "Success user", response.getDataObject()));
    }

    @PutMapping("/update-info/{username}")
    public ResponseEntity<ObjectResponse> updateAccountInfo(@PathVariable String username,
                                                            @RequestBody AccountInfo accountInfo) {
        User user = userService.findByUsername(username).orElse(null);
        if (user == null) {
            return ResponseEntity.badRequest().body(new ObjectResponse("404", "User not found", null));
        }
        logger.info("Update account info for user {}: {}", username, accountInfo.toString());
        ServiceResponse<Void> response = personService.updatePersonalInfo(user, accountInfo);
        if (response.getAckCode() != AckCodeType.SUCCESS) {
            return ResponseEntity.badRequest().body(new ObjectResponse("400", response.getMessages().getFirst(), null));
        }
        return ResponseEntity.ok(new ObjectResponse("200", "Update account info successfully", user));
    }

    @PostMapping("/update-password")
    public ResponseEntity<ObjectResponse> updateNewPassword(@Valid @RequestBody NewPasswordRequest newPasswordRequest) {
        logger.info("Update password for user {}", newPasswordRequest.getUsername());
        User user = userService.findByUsername(newPasswordRequest.getUsername()).orElse(null);
        if (user == null) {
            return ResponseEntity.badRequest().body(new ObjectResponse("404", "User not found", null));
        }
        ServiceResponse<Void> response = userService.updateNewPassword(newPasswordRequest, user);
        if (response.getAckCode() != AckCodeType.SUCCESS) {
            return ResponseEntity.badRequest().body(new ObjectResponse("400", response.getMessages().getFirst(), null));
        }
        return ResponseEntity.ok(new ObjectResponse("200", "Update password successfully", null));
    }

    @PostMapping("/update-avatar/{username}")
    public ResponseEntity<ObjectResponse> updateAvatar(@RequestParam("file") MultipartFile file,
                                                       @PathVariable String username) {
        String usernameSession = null;
        try {
            var sessionUser = JwtUtils.getSession();
            usernameSession = sessionUser.getUsername();
            if (!usernameSession.equals(username)) {
                throw new Exception("Unauthorized");
            }
        } catch (Exception e) {
            logger.error("Error: {}", e.getMessage());
            return ResponseEntity.status(401).body(new ObjectResponse("401", "Unauthorized", null));
        }
        logger.info("Update avatar for user {}: {}", username, file.getOriginalFilename());
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body(new ObjectResponse("400", "File is empty", null));
        }
        User user = userService.findByUsername(username).orElse(null);
        if (user == null) {
            return ResponseEntity.badRequest().body(new ObjectResponse("400", "User not found", null));
        }
        // delete old avatar
        if (user.getAvatar() != null) {
            storageService.deleteFile(user.getAvatar());
        }
        ServiceResponse<String> response = storageService.storeFile(file, "avatar_" + user.getId());
        if (response.getAckCode() != AckCodeType.SUCCESS) {
            return ResponseEntity.badRequest().body(new ObjectResponse("400", response.getMessages().getFirst(), null));
        }
        String fileName = response.getDataObject();
        user.setAvatar(fileName);
        userRepository.save(user);
        return ResponseEntity.ok(new ObjectResponse("200", "Update avatar success", fileName));
    }

}
