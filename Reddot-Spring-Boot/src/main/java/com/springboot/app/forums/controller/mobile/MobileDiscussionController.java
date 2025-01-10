package com.springboot.app.forums.controller.mobile;

import com.springboot.app.dto.response.ServiceResponse;
import com.springboot.app.forums.dto.request.MobileAllDiscussion;
import com.springboot.app.forums.service.mobile.MobileDiscussionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/mobile/discussions")
public class MobileDiscussionController {
    @Autowired
    private MobileDiscussionService mobileDiscussionService;

    @GetMapping("/all")
    public ResponseEntity<List<MobileAllDiscussion>> getAllDiscussions(@RequestParam(value = "title", defaultValue = "", required = false) String title) {
        ServiceResponse<List<MobileAllDiscussion>> response = mobileDiscussionService.getAllDiscussion(title);
        if (response.getDataObject() == null || response.getDataObject().isEmpty()) {
            return ResponseEntity.ok(null);
        }

        return ResponseEntity.ok(response.getDataObject());
    }
}
