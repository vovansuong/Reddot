package com.springboot.app.forums.controller;

import com.springboot.app.accounts.entity.User;
import com.springboot.app.accounts.enumeration.AccountStatus;
import com.springboot.app.accounts.repository.UserRepository;
import com.springboot.app.dto.response.AckCodeType;
import com.springboot.app.dto.response.ObjectResponse;
import com.springboot.app.dto.response.ServiceResponse;
import com.springboot.app.forums.dto.AddDiscussionRequest;
import com.springboot.app.forums.dto.DiscussionDTO;
import com.springboot.app.forums.dto.request.DiscussionCheckRole;
import com.springboot.app.forums.dto.request.DiscussionUpdateDTO;
import com.springboot.app.forums.entity.Comment;
import com.springboot.app.forums.entity.Discussion;
import com.springboot.app.forums.entity.DiscussionStat;
import com.springboot.app.forums.entity.Forum;
import com.springboot.app.forums.repository.CommentRepository;
import com.springboot.app.forums.repository.DiscussionRepository;
import com.springboot.app.forums.service.DiscussionService;
import com.springboot.app.security.jwt.JwtUtils;
import com.springboot.app.service.GenericService;
import com.springboot.app.utils.JSFUtils;
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
@RequestMapping("/api/discussions")
public class DiscussionController {

    private static final Logger logger = LoggerFactory.getLogger(DiscussionController.class);

    @Autowired
    private DiscussionService discussionService;

    @Autowired
    private GenericService genericService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private DiscussionRepository discussionRepository;
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/add")
    public ResponseEntity<ObjectResponse> addDiscussion(@Valid @RequestBody AddDiscussionRequest newDiscussion) {

        Forum forum = genericService.getEntity(Forum.class, newDiscussion.getForumId()).getDataObject();
        if (forum == null) {
            return ResponseEntity.ok(new ObjectResponse("404",
                    String.format("Forum with id %d not found", newDiscussion.getForumId()), null));
        }
        if (!forum.isActive()) {
            return ResponseEntity
                    .ok(new ObjectResponse("400", String.format("Forum %s is not active", forum.getTitle()), null));
        }
//		CommentOption commentOption = systemConfigService.getCommentOption().getDataObject();
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

        Discussion discussion = newDiscussion.getDiscussion();
        discussion.setStat(new DiscussionStat());
        discussion.setForum(forum);

        forum.getDiscussions().add(discussion);
        Comment comment = new Comment();
        comment.setContent(newDiscussion.getContent());
        comment.setIpAddress(JSFUtils.getRemoteIPAddress());

        // note: uploaded files are not supported, so we pass empty lists
        ServiceResponse<Discussion> response = discussionService.addDiscussion(discussion, comment, username);

        DiscussionDTO discussionDTO = modelMapper.map(response.getDataObject(), DiscussionDTO.class);

        if (response.getAckCode() == AckCodeType.SUCCESS) {
            return ResponseEntity.ok(new ObjectResponse("201",
                    String.format("Created Discussion %s successfully", discussion.getTitle()), discussionDTO));
        }
        return ResponseEntity.ok(new ObjectResponse("400",
                String.format("Could not create Discussion: %s", discussion.getTitle()), null));
    }

    @GetMapping("/byId/{id}")
    public ResponseEntity<ObjectResponse> getDiscussionById(@PathVariable Long id) {
        ServiceResponse<DiscussionDTO> response = discussionService.getById(id);
        if (response.getDataObject() != null && response.getDataObject().getId() != null) {
            return ResponseEntity.ok(new ObjectResponse("200", "Discussion found", response.getDataObject()));
        }
        return ResponseEntity.ok(new ObjectResponse("404", "Discussion not found", null));
    }

    @GetMapping("/byForum/{id}")
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

    @PutMapping("/update/{id}")
    public ResponseEntity<ObjectResponse> updateDiscussion(@PathVariable Long id,
                                                           @Valid @RequestBody AddDiscussionRequest newDiscussion) {
        try {
            LocalDateTime now = LocalDateTime.now();
            var userSession = JwtUtils.getSession();
            String username = userSession.getUsername();
            newDiscussion.getDiscussion().setUpdatedBy(username);
            newDiscussion.getDiscussion().setUpdatedAt(now);
        } catch (Exception e) {
            logger.error("Error getting user session: {}", e.getMessage());
        }

        Discussion oldDiscussion = genericService.findEntity(Discussion.class, id).getDataObject();
        if (oldDiscussion == null || oldDiscussion.getId() == null) {
            return ResponseEntity
                    .ok(new ObjectResponse("404", String.format("Discussion with id %d not found", id), null));
        }

        Comment comment = commentRepository.findCommentByDiscussionTitle(oldDiscussion.getTitle());
        if (comment == null || comment.getId() == null) {
            return ResponseEntity
                    .ok(new ObjectResponse("404", String.format("Comment with id %d not found", id), null));
        }

        ServiceResponse<Discussion> response = genericService.updateEntity(newDiscussion.getDiscussion());

        comment.setId(comment.getId());
        comment.setDiscussion(newDiscussion.getDiscussion());
        comment.setTitle(newDiscussion.getDiscussion().getTitle());
        comment.setContent(newDiscussion.getContent());
        comment.setUpdatedAt(LocalDateTime.now());
        comment.setUpdatedBy(newDiscussion.getDiscussion().getUpdatedBy());
        comment.setIpAddress(JSFUtils.getRemoteIPAddress());
        comment.setCommentVote(comment.getCommentVote());
        commentRepository.save(comment);

        // map discussion to discussionDTO
        DiscussionDTO discussionDTO = modelMapper.map(response.getDataObject(), DiscussionDTO.class);

        if (response.getAckCode() != AckCodeType.FAILURE) {
            return ResponseEntity.ok(new ObjectResponse("200",
                    String.format("Updated Discussion %s successfully", newDiscussion.getDiscussion().getTitle()),
                    discussionDTO));
        }
        return ResponseEntity.ok(new ObjectResponse("400",
                String.format("Could not update Discussion: %s", newDiscussion.getDiscussion().getTitle()), null));
    }

    @PutMapping("/updateDetails/{id}")
    public ResponseEntity<ObjectResponse> updateDiscussion(@PathVariable Long id,
                                                           @Valid @RequestBody DiscussionUpdateDTO discussion) {

        Discussion oldDiscussion = genericService.findEntity(Discussion.class, id).getDataObject();
        if (oldDiscussion == null || oldDiscussion.getId() == null) {
            return ResponseEntity
                    .ok(new ObjectResponse("404", String.format("Discussion with id %d not found", id), null));
        }
//		oldDiscussion.setId(id);
        oldDiscussion.setClosed(discussion.isClosed());
        oldDiscussion.setImportant(discussion.isImportant());

        try {
            LocalDateTime now = LocalDateTime.now();
            var userSession = JwtUtils.getSession();
            String username = userSession.getUsername();
            oldDiscussion.setUpdatedBy(username);
            oldDiscussion.setUpdatedAt(now);
        } catch (Exception e) {
            logger.error("Error getting user session: {}", e.getMessage());
        }

        Discussion update = discussionRepository.save(oldDiscussion);
        ServiceResponse<Discussion> response = new ServiceResponse<>();
        response.setDataObject(update);

        // map discussion to discussionDTO
        DiscussionDTO discussionDTO = modelMapper.map(response.getDataObject(), DiscussionDTO.class);

        if (response.getAckCode() != AckCodeType.FAILURE) {
            return ResponseEntity.ok(new ObjectResponse("200",
                    String.format("Updated Discussion %s successfully", discussionDTO.getTitle()),
                    discussionDTO));
        }
        return ResponseEntity.ok(new ObjectResponse("400",
                String.format("Could not update Discussion: %s", discussionDTO.getTitle()), null));
    }

    @PutMapping("/{discussionId}/tags")
    public ResponseEntity<ObjectResponse> addTagsToDiscussion(
            @PathVariable Long discussionId,
            @RequestBody List<Long> tagIds) {
        System.out.println("discussionId: " + discussionId);
        System.out.println("tagIds: " + tagIds);
        ServiceResponse<Discussion> updatedDiscussion = discussionService.addTagsToDiscussion(discussionId, tagIds);
        DiscussionDTO response = modelMapper.map(updatedDiscussion.getDataObject(), DiscussionDTO.class);

        return ResponseEntity.ok(new ObjectResponse("200", "Tags added to discussion successfully", response));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ObjectResponse> deleteDiscussion(@PathVariable Long id) {
        ServiceResponse<Void> response = discussionService.deleteDiscussion(id);
        if (response.getAckCode() == AckCodeType.SUCCESS) {
            return ResponseEntity.ok(new ObjectResponse("200", "Discussion deleted successfully", null));
        }
        return ResponseEntity.ok(new ObjectResponse("404", "Discussion not found", null));
    }

    @GetMapping("/checkRole/{discussionId}")
    public ResponseEntity<ObjectResponse> checkRole(@PathVariable Long discussionId) {
        ServiceResponse<DiscussionCheckRole> response = discussionService.checkRole(discussionId);
        if (response.getDataObject() != null && response.getDataObject().getDiscussionId() != null) {
            return ResponseEntity.ok(new ObjectResponse("200", "Role found", response.getDataObject()));
        }
        return ResponseEntity.ok(new ObjectResponse("404", "Role not found", null));
    }

}
