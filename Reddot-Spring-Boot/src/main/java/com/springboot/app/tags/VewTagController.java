package com.springboot.app.tags;

import com.springboot.app.dto.response.ObjectResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/view/tags")
public class VewTagController {

    @Autowired
    private TagService tagService;

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
}
