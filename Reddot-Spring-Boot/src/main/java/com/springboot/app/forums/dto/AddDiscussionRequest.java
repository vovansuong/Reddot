package com.springboot.app.forums.dto;

import com.springboot.app.forums.entity.Discussion;

public class AddDiscussionRequest {
    private Long forumId;
    private Discussion discussion;
    private String content;

    public Long getForumId() {
        return forumId;
    }

    public void setForumId(Long forumId) {
        this.forumId = forumId;
    }

    public Discussion getDiscussion() {
        return discussion;
    }

    public void setDiscussion(Discussion discussion) {
        this.discussion = discussion;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
