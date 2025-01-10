package com.springboot.app.admin.dto;

public class DashBoardResponse {
    private Long totalUsers;
    private Long totalForums;
    private Long totalDiscussions;
    private Long totalComments;
    private Long totalTags;

    public Long getTotalUsers() {
        return totalUsers;
    }

    public void setTotalUsers(Long totalUsers) {
        this.totalUsers = totalUsers;
    }

    public Long getTotalForums() {
        return totalForums;
    }

    public void setTotalForums(Long totalForums) {
        this.totalForums = totalForums;
    }

    public Long getTotalDiscussions() {
        return totalDiscussions;
    }

    public void setTotalDiscussions(Long totalDiscussions) {
        this.totalDiscussions = totalDiscussions;
    }

    public Long getTotalComments() {
        return totalComments;
    }

    public void setTotalComments(Long totalComments) {
        this.totalComments = totalComments;
    }

    public Long getTotalTags() {
        return totalTags;
    }

    public void setTotalTags(Long totalTags) {
        this.totalTags = totalTags;
    }
}
