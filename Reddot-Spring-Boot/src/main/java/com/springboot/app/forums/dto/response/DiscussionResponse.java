package com.springboot.app.forums.dto.response;

import lombok.Data;

@Data
public class DiscussionResponse {
    private Long discussionId;
    private String discussionTitle;

    private Long forumId;
    private String forumTitle;

    private Long forumGroupId;
    private String forumGroupTitle;

    private ViewCommentResponse commentInfo;

}
