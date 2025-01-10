package com.springboot.app.forums.controller;

import com.springboot.app.dto.response.AckCodeType;
import com.springboot.app.dto.response.ObjectResponse;
import com.springboot.app.dto.response.ServiceResponse;
import com.springboot.app.forums.dto.request.CommentVoteRequest;
import com.springboot.app.forums.service.CommentService;
import com.springboot.app.forums.service.VoteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/vote")
public class VoteController {

    @Autowired
    private VoteService voteService;
    @Autowired
    private CommentService commentService;

    @PostMapping("/vote-up")
    public ResponseEntity<ObjectResponse> voteUp(@Valid @RequestBody CommentVoteRequest comment) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if (username.equals("anonymousUser")) {
            return ResponseEntity.badRequest().body(new ObjectResponse("400", "Must login to vote", null));
        }
        ServiceResponse<Void> response = voteService.registerCommentVote(comment, username, (short) 10);

        if (response.getAckCode() == AckCodeType.SUCCESS) {
            return ResponseEntity.ok(new ObjectResponse("200", response.getMessages().getFirst(), null));
        } else {
            return ResponseEntity.ok().body(new ObjectResponse("400", response.getMessages().getFirst(), null));
        }
    }

    @PostMapping("/vote-down")
    public ResponseEntity<ObjectResponse> voteDown(@Valid @RequestBody CommentVoteRequest comment) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if (username.equals("anonymousUser")) {
            return ResponseEntity.badRequest().body(new ObjectResponse("400", "Must login to vote", null));
        }
        ServiceResponse<Void> response = voteService.registerCommentVote(comment, username, (short) -2);

        if (response.getAckCode() == AckCodeType.SUCCESS) {
            return ResponseEntity.ok(new ObjectResponse("200", response.getMessages().getFirst(), null));
        } else {
            return ResponseEntity.ok(new ObjectResponse("400", response.getMessages().getFirst(), null));
        }
    }

}
