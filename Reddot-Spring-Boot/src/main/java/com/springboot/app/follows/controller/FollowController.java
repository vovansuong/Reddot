package com.springboot.app.follows.controller;

import com.springboot.app.dto.response.AckCodeType;
import com.springboot.app.dto.response.ObjectResponse;
import com.springboot.app.dto.response.ServiceResponse;
import com.springboot.app.follows.dto.request.FollowUserRequest;
import com.springboot.app.follows.dto.response.FollowUserResponse;
import com.springboot.app.follows.service.FollowUserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/follows")
public class FollowController {
    private static final Logger logger = LoggerFactory.getLogger(FollowController.class);

    @Autowired
    private FollowUserService followUserService;

    @PostMapping("/register")
    public ResponseEntity<ObjectResponse> registerFollow(@Valid @RequestBody FollowUserRequest followUserRequest) {
        logger.info("Register follow user");
        ServiceResponse<Void> response = followUserService.registerFollow(followUserRequest);
        if (response.getAckCode() != AckCodeType.SUCCESS) {
            return ResponseEntity.badRequest().body(new ObjectResponse("400", response.getMessages().getFirst(), null));
        }
        return ResponseEntity.ok(new ObjectResponse("200", "Follow user", null));
    }

    @GetMapping("/follower/{username}")
    public ResponseEntity<ObjectResponse> getFollowUserByFollowerUserId(@PathVariable String username) {
        ServiceResponse<List<FollowUserResponse>> response = followUserService.getFollowUserByFollowerUsername(username);
        if (response.getAckCode() != AckCodeType.SUCCESS) {
            return ResponseEntity.badRequest().body(new ObjectResponse("400", response.getMessages().getFirst(), null));
        }
        logger.info("Get follow user by follower user id");
        return ResponseEntity.ok(new ObjectResponse("200", "Follow user", response.getDataObject()));
    }

    @GetMapping("/following/{username}")
    public ResponseEntity<ObjectResponse> getFollowUserByFollowingUserId(@PathVariable String username) {
        logger.info("Get follow user by following user id");
        ServiceResponse<List<FollowUserResponse>> response = followUserService.getFollowUserByFollowingUsername(username);
        if (response.getAckCode() != AckCodeType.SUCCESS) {
            return ResponseEntity.badRequest().body(new ObjectResponse("400", response.getMessages().getFirst(), null));
        }
        return ResponseEntity.ok(new ObjectResponse("200", "Follow user", response.getDataObject()));
    }


}
