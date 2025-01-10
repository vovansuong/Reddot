package com.springboot.app.accounts.controller;

import com.springboot.app.admin.dto.DataForumGroupResponse;
import com.springboot.app.repository.DiscussionDAO;
import com.springboot.app.security.jwt.JwtUtils;
import jakarta.annotation.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

//@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/test")
public class TestController {

    @Resource
    private DiscussionDAO discussionDAO;

    @GetMapping("/all")
    public String allAccess() {
        var session = JwtUtils.getSession();
        return "Public Content. " + session;
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public String userAccess() {
        var session = JwtUtils.getSession();
        return "User Content. " + session;
    }

    @GetMapping("/mod")
    @PreAuthorize("hasRole('MODERATOR')")
    public String moderatorAccess() {
        var session = JwtUtils.getSession();
        return "Moderator Board. " + session;
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminAccess() {
        var session = JwtUtils.getSession();
        return "Admin Board. " + session;
    }

    @GetMapping("/stats")
    public ResponseEntity<List<DataForumGroupResponse>> getForumStats() {
        List<DataForumGroupResponse> response = discussionDAO.getForumGroupData();
        return ResponseEntity.ok(response);
    }


}
