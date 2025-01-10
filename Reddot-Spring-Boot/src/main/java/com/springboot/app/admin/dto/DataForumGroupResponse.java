package com.springboot.app.admin.dto;

public class DataForumGroupResponse {
    private String name; //forum group name
    private Long discussions;
    private Long comments;
    private Long users;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getDiscussions() {
        return discussions;
    }

    public void setDiscussions(Long discussions) {
        this.discussions = discussions;
    }

    public Long getComments() {
        return comments;
    }

    public void setComments(Long comments) {
        this.comments = comments;
    }

    public Long getUsers() {
        return users;
    }

    public void setUsers(Long users) {
        this.users = users;
    }


}
