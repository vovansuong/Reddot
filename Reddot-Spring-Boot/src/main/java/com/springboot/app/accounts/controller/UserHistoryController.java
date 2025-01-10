package com.springboot.app.accounts.controller;

import com.springboot.app.accounts.service.UserHistoryService;
import com.springboot.app.dto.response.ObjectResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user-history")
public class UserHistoryController {

    @Autowired
    private UserHistoryService userHistoryService;

    @GetMapping("/bookmarks")
    public ResponseEntity<?> getBookmarksByUsername(
            @RequestParam(value = "page", defaultValue = "1", required = false) int page,
            @RequestParam(value = "size", defaultValue = "10", required = false) int size,
            @RequestParam(value = "orderBy", defaultValue = "id", required = false) String orderBy,
            @RequestParam(value = "sort", defaultValue = "DESC", required = false) String sort,
            @RequestParam(value = "username", defaultValue = "", required = true) String username
    ) {
        if (username.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(new ObjectResponse("400", "Username is required", null));
        }
        return ResponseEntity.ok(userHistoryService.getAllBookmarksByUsername(page, size, orderBy, sort, username));
    }

    @GetMapping("/comments")
    public ResponseEntity<?> getCommentsByUsername(
            @RequestParam(value = "page", defaultValue = "1", required = false) int page,
            @RequestParam(value = "size", defaultValue = "10", required = false) int size,
            @RequestParam(value = "orderBy", defaultValue = "id", required = false) String orderBy,
            @RequestParam(value = "sort", defaultValue = "DESC", required = false) String sort,
            @RequestParam(value = "username", defaultValue = "", required = true) String username
    ) {
        if (username.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(new ObjectResponse("400", "Username is required", null));
        }
        return ResponseEntity.ok(userHistoryService.getAllCommentsByUsername(page, size, orderBy, sort, username));
    }
}
