package com.springboot.app.forums.controller;

import com.springboot.app.accounts.entity.User;
import com.springboot.app.accounts.enumeration.AccountStatus;
import com.springboot.app.accounts.repository.UserRepository;
import com.springboot.app.dto.response.AckCodeType;
import com.springboot.app.dto.response.ObjectResponse;
import com.springboot.app.dto.response.ServiceResponse;
import com.springboot.app.forums.dto.AddCommentRequest;
import com.springboot.app.forums.dto.CommentDTO;
import com.springboot.app.forums.entity.Comment;
import com.springboot.app.forums.entity.Discussion;
import com.springboot.app.forums.repository.CommentRepository;
import com.springboot.app.forums.service.CommentService;
import com.springboot.app.security.jwt.JwtUtils;
import com.springboot.app.service.GenericService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    private static final Logger logger = LoggerFactory.getLogger(CommentController.class);

    @Autowired
    private GenericService genericService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private CommentService commentService;
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/add")
    public ResponseEntity<ObjectResponse> addComment(@Valid @RequestBody AddCommentRequest newComment) {
        Discussion discussion = genericService.getEntity(Discussion.class, newComment.getDiscussionId())
                .getDataObject();
        if (discussion == null) {
            return ResponseEntity.ok(new ObjectResponse("404",
                    String.format("Discussion with id %s not found", newComment.getDiscussionId()), null));
        }
        String username = null;
        try {
            var userSession = JwtUtils.getSession();
            username = userSession.getUsername();
        } catch (Exception e) {
            logger.error("Error getting user session: {}", e.getMessage());
        }

        User user = userRepository.findByUsername(username).orElse(null);
        if (user != null && user.getAccountStatus().equals(AccountStatus.LOCKED)) {
            return ResponseEntity.ok(new ObjectResponse("407",
                    String.format("User with username %s is locked", username), null));
        }


        ServiceResponse<Comment> response = commentService.addComment(discussion.getId(), newComment.getComment(),
                username, newComment.getReplyToId());
        CommentDTO commentDTO = modelMapper.map(response.getDataObject(), CommentDTO.class);
        if (response.getAckCode() == AckCodeType.SUCCESS) {
            return ResponseEntity.ok(new ObjectResponse("201",
                    String.format("Added comment %s successfully", newComment.getComment().getTitle()), commentDTO));
        }

        return ResponseEntity.ok(new ObjectResponse("400",
                String.format("Could not add comment: %s", newComment.getComment().getTitle()), null));
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<ObjectResponse> updateComment(@PathVariable Long id, @Valid @RequestBody CommentDTO commentDTO) {
        Comment comment = genericService.getEntity(Comment.class, id).getDataObject();
        try {
            var userSession = JwtUtils.getSession();
            String username = userSession.getUsername();
            comment.setUpdatedAt(LocalDateTime.now());
            comment.setUpdatedBy(username);
        } catch (Exception e) {
            logger.error("Error getting user session: {}", e.getMessage());
        }
        if (comment == null) {
            return ResponseEntity.ok(new ObjectResponse("404", String.format("Comment with id %s not found", id), null));
        }
        comment.setTitle(commentDTO.getTitle());
        comment.setContent(commentDTO.getContent());

        comment = commentRepository.save(comment);
        commentDTO = modelMapper.map(comment, CommentDTO.class);
        return ResponseEntity.ok(new ObjectResponse("200", String.format("Updated comment %s successfully", id), commentDTO));
    }

    @DeleteMapping("/delete/{id}/{discussionId}")
    public ResponseEntity<ObjectResponse> deleteComment(@PathVariable Long id, @PathVariable Long discussionId) {
        ServiceResponse<Comment> comment = commentService.deleteComment(id, discussionId);
        if (comment == null) {
            return ResponseEntity.ok(new ObjectResponse("404", String.format("Comment with id %s not found", id), null));
        }
        return ResponseEntity.ok(new ObjectResponse("200", String.format("Deleted comment %s successfully", id), null));
    }

    @GetMapping("/all")
    public ResponseEntity<ObjectResponse> getAllComments() {
        ServiceResponse<List<CommentDTO>> response = commentService.getAllComment();
        if (response.getAckCode() == AckCodeType.SUCCESS) {
            return ResponseEntity.ok(new ObjectResponse("200", "Get all comments successfully", response.getDataObject()));
        }
        return ResponseEntity.ok(new ObjectResponse("400", "Could not get all comments", null));
    }


}
