package com.springboot.app.forums.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MobileDiscussionResponse {
    //discussion
    private Long discussionId;
    private String discussionTitle;
    private LocalDateTime createdAt;
    //author
    private String username; //author username
    private String name; //author name
    private String imageUrl; //author image url
    private String avatar;
}
