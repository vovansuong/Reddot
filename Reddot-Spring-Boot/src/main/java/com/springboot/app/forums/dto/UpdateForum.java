package com.springboot.app.forums.dto;

import com.springboot.app.forums.entity.Forum;

public class UpdateForum {
    private Long forumGroupId;
    private Forum forum;

    public Long getForumGroupId() {
        return forumGroupId;
    }

    public void setForumGroupId(Long forumGroupId) {
        this.forumGroupId = forumGroupId;
    }

    public Forum getForum() {
        return forum;
    }

    public void setForum(Forum forum) {
        this.forum = forum;
    }
}
