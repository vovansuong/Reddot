package com.springboot.app.bagdes;

import com.springboot.app.accounts.entity.User;
import com.springboot.app.accounts.repository.UserRepository;
import com.springboot.app.dto.response.AckCodeType;
import com.springboot.app.dto.response.ObjectResponse;
import com.springboot.app.dto.response.ServiceResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/badges")
public class BadgeController {
    private static final Logger logger = LoggerFactory.getLogger(BadgeController.class);

    @Autowired
    private BadgeService badgeService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public ResponseEntity<ObjectResponse> getAllBadges() {
        logger.info("Get all badges success");
        ServiceResponse<List<Badge>> response = badgeService.getAllBadges();
        if (response.getAckCode() != AckCodeType.SUCCESS) {
            return ResponseEntity.ok(new ObjectResponse("400", "Get all badges fail", null));
        }
        return ResponseEntity.ok(new ObjectResponse("200", "Get all badges success", response.getDataObject()));
    }

    // update badge
    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ObjectResponse> updateBadge(@PathVariable Long id, @Valid @RequestBody Badge badge) {
        ServiceResponse<Void> response = badgeService.updateBadge(id, badge);
        if (response.getAckCode() != AckCodeType.SUCCESS) {
            return ResponseEntity.ok(new ObjectResponse("400", "Update badge fail", null));
        }
        logger.info("Update badge success");
        return ResponseEntity.ok(new ObjectResponse("200", "Update badge success", null));
    }

    // set badge for user
    @GetMapping("/set-badge/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ObjectResponse> setBadgeForUser(@PathVariable Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return ResponseEntity.ok(new ObjectResponse("400", "User not found", null));
        }
        logger.info("Set badge for user");
        ServiceResponse<Void> response = badgeService.setBadgeForUser(user);
        if (response.getAckCode() != AckCodeType.SUCCESS) {
            return ResponseEntity.ok(new ObjectResponse("400", "Set badge for user fail", null));
        }
        return ResponseEntity.ok(new ObjectResponse("200", "Set badge for user success", null));
    }

    // set badge for all user
    @GetMapping("/set-all-user")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ObjectResponse> setBadgeForAllUser() {
        logger.info("Set badge for all user");
        ServiceResponse<Void> response = badgeService.setBadgeForAllUser();
        if (response.getAckCode() != AckCodeType.SUCCESS) {
            return ResponseEntity.ok(new ObjectResponse("400", "Set badge for all user fail", null));
        }
        return ResponseEntity.ok(new ObjectResponse("200", "Set badge for all user success", null));
    }
}
