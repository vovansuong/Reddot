package com.springboot.app.tags;

import com.springboot.app.dto.response.AckCodeType;
import com.springboot.app.dto.response.ObjectResponse;
import com.springboot.app.dto.response.ServiceResponse;
import com.springboot.app.security.jwt.JwtUtils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/admin/tags")
public class TagManagementController {
    private static final Logger logger = LoggerFactory.getLogger(TagManagementController.class.getName());

    @Autowired
    private TagService tagService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("")
    public ResponseEntity<ObjectResponse> getAllTags(
            @RequestParam(value = "page", defaultValue = "1", required = false) int page,
            @RequestParam(value = "size", defaultValue = "10", required = false) int size,
            @RequestParam(value = "orderBy", defaultValue = "id", required = false) String orderBy,
            @RequestParam(value = "sort", defaultValue = "ASC", required = false) String sort,
            @RequestParam(value = "search", defaultValue = "", required = false) String search) {
        return ResponseEntity
                .ok(new ObjectResponse("201", "Success", tagService.getAllTags(page, size, orderBy, sort, search)));
    }

    @PostMapping("/create")
    public ResponseEntity<ObjectResponse> createNewTag(@RequestBody Tag newTag) {
        logger.info("Creating new tag: {}", newTag.getLabel());

        try {
            var userSession = JwtUtils.getSession();
            String username = userSession.getUsername();
            newTag.setCreatedBy(username);
        } catch (Exception e) {
            logger.error("Error getting user session: {}", e.getMessage());
        }
        ServiceResponse<Tag> response = tagService.createNewTag(newTag);
        TagDTO tagDTO = modelMapper.map(response.getDataObject(), TagDTO.class);

        if (response.getAckCode() != AckCodeType.SUCCESS) {
            return ResponseEntity.ok(new ObjectResponse("400", "Tag not created", tagDTO));
        }
        return ResponseEntity
                .ok(new ObjectResponse("201", String.format("Tag %s created successfully", newTag.getLabel()), tagDTO));

    }

    @PutMapping("/update")
    public ResponseEntity<ObjectResponse> updateTag(@RequestBody Tag tagToUpdate) {
        logger.info("Updating tag: {}", tagToUpdate.getLabel());
        try {
            var userSession = JwtUtils.getSession();
            String username = userSession.getUsername();
            tagToUpdate.setUpdatedBy(username);
            tagToUpdate.setUpdatedAt(LocalDateTime.now());
        } catch (Exception e) {
            logger.error("Error getting user session: {}", e.getMessage());
        }
        ServiceResponse<Tag> response = tagService.updateTag(tagToUpdate);
        TagDTO tagDTO = modelMapper.map(response.getDataObject(), TagDTO.class);
        if (response.getAckCode() != AckCodeType.SUCCESS) {
            return ResponseEntity.ok(new ObjectResponse("400", "Tag not updated", null));
        }
        return ResponseEntity.ok(
                new ObjectResponse("200", String.format("Tag %s update successfully", tagToUpdate.getLabel()), tagDTO));
    }
}
