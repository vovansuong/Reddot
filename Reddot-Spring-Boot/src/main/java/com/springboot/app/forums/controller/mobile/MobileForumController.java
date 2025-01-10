package com.springboot.app.forums.controller.mobile;

import com.springboot.app.dto.response.AckCodeType;
import com.springboot.app.dto.response.ServiceResponse;
import com.springboot.app.forums.dto.request.MobileCommentRequest;
import com.springboot.app.forums.dto.request.MobileDiscussionRequest;
import com.springboot.app.forums.dto.response.MobileDiscussionResponse;
import com.springboot.app.forums.dto.response.MobileForumResponse;
import com.springboot.app.forums.dto.response.MobileGroupResponse;
import com.springboot.app.forums.dto.response.ViewCommentResponse;
import com.springboot.app.forums.service.mobile.MobileForumsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mobile/forum")
public class MobileForumController {

    private static final Logger logger = LoggerFactory.getLogger(MobileForumController.class);

    @Autowired
    private MobileForumsService mobileForumsService;

    @GetMapping("/groups")
    public ResponseEntity<List<MobileGroupResponse>> getAllForumGroups() {
        ServiceResponse<List<MobileGroupResponse>> response = mobileForumsService.getAllForumGroups();
        return ResponseEntity.ok(response.getDataObject());
    }

    @GetMapping("/forums-info/all")
    public ResponseEntity<List<MobileForumResponse>> getAllForumByAllGroup() {
        ServiceResponse<List<MobileForumResponse>> response = mobileForumsService.getAllForumByAllGroup();
        return ResponseEntity.ok(response.getDataObject());
    }

    @GetMapping("/forums-info/{id}")
    public ResponseEntity<List<MobileForumResponse>> getAllForumByGroupId(@PathVariable Long id) {
        ServiceResponse<List<MobileForumResponse>> response = mobileForumsService.getAllForumsByGroupId(id);
        return ResponseEntity.ok(response.getDataObject());
    }

    @GetMapping("/discussions/{id}")
    public ResponseEntity<List<ViewCommentResponse>> getAllCommentByDiscussionId(@PathVariable Long id) {
        ServiceResponse<List<ViewCommentResponse>> response = mobileForumsService.getAllCommentByDiscussionId(id);
        return ResponseEntity.ok(response.getDataObject());
    }

    @GetMapping("/content/{id}")
    public ResponseEntity<byte[]> getContentByCommentId(@PathVariable Long id) {
        ServiceResponse<byte[]> response = mobileForumsService.getContentByCommentId(id);
        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_HTML)
                .body(response.getDataObject());
    }


    //create a new discussion
    @PostMapping("/discussions/add")
    public ResponseEntity<MobileDiscussionResponse> addDiscussion(@RequestBody MobileDiscussionRequest newDiscussion) {
        logger.info("MobileDiscussionController.addDiscussion() called");
        ServiceResponse<MobileDiscussionResponse> response = mobileForumsService.addNewDiscussion(newDiscussion);
        if (response.getAckCode() != AckCodeType.SUCCESS) {
            return ResponseEntity.badRequest().body(null);
        } else {
            return ResponseEntity.ok(response.getDataObject());
        }
    }

    //create a new comment
    @PostMapping("/comments/add")
    public ResponseEntity<ViewCommentResponse> addComment(@RequestBody MobileCommentRequest newComment) {
        logger.info("MobileDiscussionController.addComment() called");
        ServiceResponse<ViewCommentResponse> response = mobileForumsService.addNewComment(newComment);
        if (response.getAckCode() != AckCodeType.SUCCESS) {
            return ResponseEntity.badRequest().body(null);
        } else {
            return ResponseEntity.ok(response.getDataObject());
        }
    }
}
