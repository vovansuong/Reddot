package com.springboot.app.forums.dto.request;

import lombok.Data;

@Data
public class DiscussionCheckRole {
    private String roleName;
    private Long discussionId;
}
