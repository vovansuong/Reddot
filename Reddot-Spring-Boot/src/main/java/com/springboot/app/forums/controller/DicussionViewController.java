package com.springboot.app.forums.controller;

import com.springboot.app.dto.response.AckCodeType;
import com.springboot.app.dto.response.ObjectResponse;
import com.springboot.app.dto.response.ServiceResponse;
import com.springboot.app.forums.dto.DiscussionDTO;
import com.springboot.app.forums.dto.request.LastComment;
import com.springboot.app.forums.dto.response.DiscussionResponse;
import com.springboot.app.forums.service.CommentService;
import com.springboot.app.forums.service.DiscussionService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/view/discussions")
public class DicussionViewController {

    private static final Logger logger = LoggerFactory.getLogger(DicussionViewController.class);

    @Autowired
    private DiscussionService discussionService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("/byId/{id}")
    public ResponseEntity<ObjectResponse> getDiscussionById(@PathVariable Long id) {
        ServiceResponse<DiscussionDTO> response = discussionService.getById(id);
        if (response.getDataObject() != null && response.getDataObject().getId() != null) {
            return ResponseEntity.ok(new ObjectResponse("200", "Discussion found", response.getDataObject()));
        }
        return ResponseEntity.ok(new ObjectResponse("404", "Discussion not found", null));
    }

    @GetMapping("/byFourm/{id}")
    public ResponseEntity<ObjectResponse> getDiscussionsByForum(Long id) {
        ServiceResponse<List<DiscussionDTO>> response = discussionService.getDiscussionsByForum(id);
        if (response.getDataObject() != null && !response.getDataObject().isEmpty()) {
            return ResponseEntity.ok(new ObjectResponse("200", "Discussions found", response.getDataObject()));
        }
        return ResponseEntity.ok(new ObjectResponse("404", "Discussions not found", null));
    }

    @GetMapping("/all")
    public ResponseEntity<ObjectResponse> getAllDiscussions() {
        ServiceResponse<List<DiscussionDTO>> response = discussionService.getAllDiscussions();
        if (response.getDataObject() != null && !response.getDataObject().isEmpty()) {
            return ResponseEntity.ok(new ObjectResponse("200", "Discussions found", response.getDataObject()));
        }
        return ResponseEntity.ok(new ObjectResponse("404", "Discussions not found", null));
    }

    @GetMapping("/details")
    public ResponseEntity<?> getDiscussionDetails(
            @RequestParam(value = "page", defaultValue = "1", required = false) int page,
            @RequestParam(value = "size", defaultValue = "10", required = false) int size,
            @RequestParam(value = "orderBy", defaultValue = "id", required = false) String orderBy,
            @RequestParam(value = "sort", defaultValue = "ASC", required = false) String sort,
            @RequestParam(value = "discussionId", defaultValue = "", required = false) Long discussionId) {
        if (discussionId == null || discussionId == 0) {
            return ResponseEntity.badRequest().body(new ObjectResponse("400", "Discussion ID is required", null));
        }
        discussionService.updateDiscussionViews(discussionId);
        return ResponseEntity.ok(commentService.getAllCommentsByDiscussionId(page, size, orderBy, sort, discussionId));
    }

    @GetMapping("/first-comment/{discussionId}")
    public ResponseEntity<ObjectResponse> getFirstCommentByDiscussionId(@PathVariable Long discussionId) {
        if (discussionId == null || discussionId == 0) {
            return ResponseEntity.badRequest().body(new ObjectResponse("400", "Discussion ID is required", null));
        }
        ServiceResponse<DiscussionResponse> response = commentService.getFirstCommentByDiscussionId(discussionId);
        if (response.getAckCode() != AckCodeType.SUCCESS) {
            return ResponseEntity.ok(new ObjectResponse("404", "First comment not found", null));
        } else {
            return ResponseEntity.ok(new ObjectResponse("200", "First comment found", response.getDataObject()));
        }
    }

    @GetMapping("")
    public ResponseEntity<ObjectResponse> getPageDiscussions(
            @RequestParam(value = "page", defaultValue = "1", required = false) int page,
            @RequestParam(value = "size", defaultValue = "10", required = false) int size,
            @RequestParam(value = "orderBy", defaultValue = "id", required = false) String orderBy,
            @RequestParam(value = "sort", defaultValue = "ASC", required = false) String sort,
            @RequestParam(value = "search", defaultValue = "", required = false) String search,
            @RequestParam(value = "forumId", defaultValue = "", required = false) Long forumId) {
        return ResponseEntity.ok(new ObjectResponse("200", "Success",
                discussionService.getAllDiscussion(page, size, orderBy, sort, search, forumId)));
    }

    @GetMapping("/tag/{tagId}")
    public ResponseEntity<ObjectResponse> getDiscussionsByTagId(@PathVariable Long tagId) {
        ServiceResponse<List<DiscussionDTO>> response = discussionService.getDiscussionsByTagId(tagId);
        if (response.getDataObject() != null && !response.getDataObject().isEmpty()) {
            return ResponseEntity.ok(new ObjectResponse("200", "Discussions found", response));
        }
        return ResponseEntity.ok(new ObjectResponse("404", "Discussions not found", null));
    }

    @GetMapping("/getLastCommentServiceResponseDiscussion/{id}")
    public ResponseEntity<ObjectResponse> getLastCommentServiceResponse(@PathVariable("id") Long id) {
        ServiceResponse<LastComment> response = discussionService.getLatCommentServiceResponse(id);
        if (response.getDataObject() == null) {
            return ResponseEntity.ok(new ObjectResponse("404", "Data not found", null));
        }
        return ResponseEntity.ok(new ObjectResponse("200", "Data", response.getDataObject()));
    }
}
