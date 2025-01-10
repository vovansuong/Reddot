package com.springboot.app.forums.dto;

import com.springboot.app.forums.entity.ForumGroup;

public class AddForumGroupDTO {
    private ForumGroup forumGroup;
    private String roleName;

    public ForumGroup getForumGroup() {
        return forumGroup;
    }

    public void setForumGroup(ForumGroup forumGroup) {
        this.forumGroup = forumGroup;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

}
