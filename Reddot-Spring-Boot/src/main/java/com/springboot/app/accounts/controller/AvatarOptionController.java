package com.springboot.app.accounts.controller;

import com.springboot.app.accounts.dto.request.AvatarOptionRequest;
import com.springboot.app.accounts.service.AvatarOptionService;
import com.springboot.app.dto.response.AckCodeType;
import com.springboot.app.dto.response.ObjectResponse;
import com.springboot.app.dto.response.ServiceResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/avatar-options")
public class AvatarOptionController {
    private static final Logger logger = LoggerFactory.getLogger(AvatarOptionController.class);

    @Autowired
    private AvatarOptionService avatarOptionService;

    @GetMapping
    public ResponseEntity<ObjectResponse> getAllAvatarOptions() {
        logger.info("Get all avatar options success");
        return ResponseEntity.ok(new ObjectResponse("200", "Get all avatar options success", avatarOptionService.getAvatarOption()));
    }

    @PostMapping("/update")
    public ResponseEntity<ObjectResponse> updateAvatarOption(@RequestBody AvatarOptionRequest avatarOption) {
        logger.info("Update avatar option");
        ServiceResponse<Void> response = avatarOptionService.updateAvatarOption(avatarOption);
        if (response.getAckCode() != AckCodeType.SUCCESS) {
            return ResponseEntity.badRequest().body(new ObjectResponse("400", "Update avatar option fail", null));
        }
        logger.info("Update avatar option success");
        return ResponseEntity.ok(new ObjectResponse("200", "Update avatar option success", null));
    }
}
