package com.springboot.app.forums.controller;

import com.springboot.app.dto.response.ServiceResponse;
import com.springboot.app.forums.dto.response.ForumGroupStat;
import com.springboot.app.forums.service.ForumStatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/view/forum-stat")
public class ForumStatController {

    @Autowired
    private ForumStatService forumStatService;

    @GetMapping("")
    public ResponseEntity<ForumGroupStat> getForumGroupStat() {
        ServiceResponse<ForumGroupStat> forumGroupStat = forumStatService.getForumGroupStat();

        return ResponseEntity.ok(forumGroupStat.getDataObject());
    }
}
