package com.springboot.app.follows.controller;

import com.springboot.app.dto.response.AckCodeType;
import com.springboot.app.dto.response.ObjectResponse;
import com.springboot.app.dto.response.ServiceResponse;
import com.springboot.app.follows.dto.request.BookmarkRequest;
import com.springboot.app.follows.service.BookmarkService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/bookmarks")
public class BookmarkController {

    private static final Logger logger = LoggerFactory.getLogger(BookmarkController.class);

    @Autowired
    private BookmarkService bookmarkService;

    @PostMapping("/register")
    public ResponseEntity<ObjectResponse> registerBookmark(@Valid @RequestBody BookmarkRequest request) {
        ServiceResponse<Void> response = bookmarkService.registerBookmark(request);
        if (response.getAckCode() != AckCodeType.SUCCESS) {
            return ResponseEntity.badRequest().body(new ObjectResponse("400", response.getMessages().getFirst(), null));
        }
        return ResponseEntity.ok(new ObjectResponse("200", "Bookmark created successfully", null));
    }
}
