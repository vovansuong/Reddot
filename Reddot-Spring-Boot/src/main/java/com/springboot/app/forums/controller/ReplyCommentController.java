package com.springboot.app.forums.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reply-comments")
public class ReplyCommentController {
    private static final Logger logger = LoggerFactory.getLogger(ReplyCommentController.class.getName());

}
