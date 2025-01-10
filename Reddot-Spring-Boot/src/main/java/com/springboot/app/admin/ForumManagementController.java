package com.springboot.app.admin;

import com.springboot.app.dto.response.AckCodeType;
import com.springboot.app.dto.response.ObjectResponse;
import com.springboot.app.dto.response.ServiceResponse;
import com.springboot.app.forums.dto.AddForumGroupDTO;
import com.springboot.app.forums.dto.ForumDTO;
import com.springboot.app.forums.dto.UpdateForum;
import com.springboot.app.forums.entity.Forum;
import com.springboot.app.forums.entity.ForumGroup;
import com.springboot.app.forums.repository.ForumRepository;
import com.springboot.app.forums.service.ForumService;
import com.springboot.app.security.jwt.JwtUtils;
import com.springboot.app.service.GenericService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class ForumManagementController {

    private static final Logger logger = LoggerFactory.getLogger(ForumManagementController.class);

    @Autowired
    private ForumService forumService;

    @Autowired
    private GenericService genericService;

    @Autowired
    private ForumRepository forumRepository;

    @Autowired
    private ModelMapper modelMapper;

    @PostMapping("/forum-groups")
    public ResponseEntity<ObjectResponse> createForumGroup(@Valid @RequestBody AddForumGroupDTO newForumGroup,
                                                           BindingResult bindingResult) {
        try {
            var userSession = JwtUtils.getSession();
            String username = userSession.getUsername();
            newForumGroup.getForumGroup().setCreatedBy(username);
        } catch (Exception e) {
            logger.error("Error getting user session: {}", e.getMessage());
        }
        // validation newForumGroup
        if (bindingResult.hasErrors()) {
            return ResponseEntity.ok(new ObjectResponse("400", "Invalid Forum Group data", null));
        }

        ServiceResponse<ForumGroup> response = forumService.addForumGroup(newForumGroup.getForumGroup(),
                newForumGroup.getRoleName());

        if (response.getAckCode() == AckCodeType.SUCCESS) {
            return ResponseEntity.ok(new ObjectResponse("201",
                    String.format("Created Forum Group %s successfully", newForumGroup.getForumGroup().getTitle()),
                    response.getDataObject()));
        }
        return ResponseEntity.ok(new ObjectResponse("400",
                String.format("Could not create Forum Group: %s", newForumGroup.getForumGroup().getTitle()), null));
    }

    @DeleteMapping("/forum-groups/delete/{id}")
    public ResponseEntity<ObjectResponse> deleteForumGroup(@PathVariable Long id) {
        ForumGroup forumGroup = genericService.findEntity(ForumGroup.class, id).getDataObject();
        if (forumGroup == null) {
            return ResponseEntity
                    .ok(new ObjectResponse("404", String.format("Forum Group with id %d not found", id), null));
        }

        List<Forum> forum = forumRepository.findForumByForumGroupId(id);
        if (!forum.isEmpty()) {
            return ResponseEntity.ok(new ObjectResponse("202",
                    String.format("Could not delete Forum Group: %s, it has forums associated with it",
                            forumGroup.getTitle()),
                    null));
        }

        ServiceResponse<Void> response = forumService.deleteForumGroup(forumGroup);
        if (response.getAckCode() != AckCodeType.FAILURE) {
            return ResponseEntity.ok(new ObjectResponse("200",
                    String.format("Deleted Forum Group %s successfully", forumGroup.getTitle()), null));
        } else {
            return ResponseEntity.ok(new ObjectResponse("400",
                    String.format("Could not delete Forum Group: %s", forumGroup.getTitle()), null));
        }
    }

    @PutMapping("/forum-groups/update/{id}")
    public ResponseEntity<ObjectResponse> editForumGroup(@PathVariable Long id,
                                                         @RequestBody AddForumGroupDTO newForumGroup) {
        try {
            LocalDateTime now = LocalDateTime.now();
            var userSession = JwtUtils.getSession();
            String username = userSession.getUsername();
            newForumGroup.getForumGroup().setUpdatedBy(username);
            newForumGroup.getForumGroup().setUpdatedAt(now);
        } catch (Exception e) {
            logger.error("Error getting user session: {}", e.getMessage());
        }
        ForumGroup oldForumGroup = genericService.findEntity(ForumGroup.class, id).getDataObject();
        if (oldForumGroup == null) {
            return ResponseEntity
                    .ok(new ObjectResponse("404", String.format("Forum Group with id %d not found", id), null));
        }

        newForumGroup.getForumGroup().setManager(newForumGroup.getRoleName());

        ServiceResponse<ForumGroup> response = genericService.updateEntity(newForumGroup.getForumGroup());
        if (response.getAckCode() != AckCodeType.FAILURE) {
            return ResponseEntity.ok(new ObjectResponse("200",
                    String.format("Updated Forum Group %s successfully", newForumGroup.getForumGroup().getTitle()),
                    response.getDataObject()));
        }
        return ResponseEntity.ok(new ObjectResponse("400",
                String.format("Could not update Forum Group: %s", newForumGroup.getForumGroup().getTitle()), null));

    }

    @PostMapping("/forums")
    public ResponseEntity<ObjectResponse> createForum(@RequestBody ForumDTO newForum) {
        String username = null;
        try {
            var userSession = JwtUtils.getSession();
            username = userSession.getUsername();
            newForum.setCreatedBy(username);
        } catch (Exception e) {
            logger.error("Error getting user session: {}", e.getMessage());
        }

        if (newForum.getIdForumGroup() == null) {
            return ResponseEntity.ok(new ObjectResponse("400", "Forum Group ID is required", null));
        }

        Forum newForumEntity = new Forum();
        newForumEntity.setTitle(newForum.getTitle());
        newForumEntity.setDescription(newForum.getDescription());
        newForumEntity.setIcon(newForum.getIcon());
        newForumEntity.setColor(newForum.getColor());
        newForumEntity.setCreatedBy(newForum.getCreatedBy());

        ForumGroup forumGroup = genericService.findEntity(ForumGroup.class, newForum.getIdForumGroup()).getDataObject();

        ServiceResponse<ForumDTO> response = forumService.addForum(newForumEntity, forumGroup, username);
        if (response.getAckCode() == AckCodeType.SUCCESS) {
            return ResponseEntity.ok(new ObjectResponse("201",
                    String.format("Created Forum %s successfully", newForum.getTitle()), response.getDataObject()));
        }
        return ResponseEntity
                .ok(new ObjectResponse("400", String.format("Could not create Forum: %s", newForum.getTitle()), null));
    }

    @DeleteMapping("/forums/{id}")
    public ResponseEntity<ObjectResponse> deleteForum(@PathVariable Long id) {
        Forum forum = genericService.findEntity(Forum.class, id).getDataObject();
        if (forum == null) {
            return ResponseEntity.ok(new ObjectResponse("404", String.format("Forum with id %d not found", id), null));
        }

        ServiceResponse<Void> response = forumService.deleteForum(forum);
        if (response.getAckCode() != AckCodeType.FAILURE) {
            return ResponseEntity.ok(
                    new ObjectResponse("200", String.format("Deleted Forum %s successfully", forum.getTitle()), null));
        }
        return ResponseEntity
                .ok(new ObjectResponse("400", String.format("Could not delete Forum: %s", forum.getTitle()), null));
    }

    @PatchMapping("/forums/update/{id}")
    public ResponseEntity<ObjectResponse> editForum(@PathVariable Long id, @RequestBody UpdateForum newForum) {
        try {
            LocalDateTime now = LocalDateTime.now();
            var userSession = JwtUtils.getSession();
            String username = userSession.getUsername();
            newForum.getForum().setUpdatedBy(username);
            newForum.getForum().setUpdatedAt(now);
        } catch (Exception e) {
            logger.error("Error getting user session: {}", e.getMessage());
        }
        Forum oldForum = genericService.findEntity(Forum.class, id).getDataObject();
        if (oldForum == null) {
            return ResponseEntity.ok(new ObjectResponse("404", String.format("Forum with id %d not found", id), null));
        }

        ForumGroup forumGroup = genericService.findEntity(ForumGroup.class, newForum.getForumGroupId()).getDataObject();
        if (forumGroup == null) {
            return ResponseEntity.ok(new ObjectResponse("404",
                    String.format("Forum Group with id %d not found", newForum.getForumGroupId()), null));
        }

        newForum.getForum().setForumGroup(forumGroup);

        ServiceResponse<Forum> response = genericService.updateEntity(newForum.getForum());

        // map newForum to ForumDTO
        ForumDTO newForumDTO = modelMapper.map(newForum, ForumDTO.class);

        if (response.getAckCode() != AckCodeType.FAILURE) {
            return ResponseEntity.ok(new ObjectResponse("200",
                    String.format("Updated Forum %s successfully", newForum.getForum().getTitle()), newForumDTO));
        }
        return ResponseEntity.ok(new ObjectResponse("400",
                String.format("Could not update Forum: %s", newForum.getForum().getTitle()), null));
    }

    @PostMapping("/forum-groups/vote-sort-by-order-forum-group/{id}/{type}")
    public ResponseEntity<ObjectResponse> voteSortByOrderForumGroup(@PathVariable("id") Long id, @PathVariable("type") String type) {
        ServiceResponse<List<ForumGroup>> response = forumService.voteSortByOrderForumGroup(id, type);
        if (response.getAckCode() == AckCodeType.FAILURE) {
            return ResponseEntity.ok(new ObjectResponse("400", "Could not vote sort by order forum group", null));
        }
        return ResponseEntity.ok(new ObjectResponse("200", "Success", null));
    }
}
