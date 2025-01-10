package com.springboot.app.follows.dto.request;

public class FollowUserRequest {

    // This is the id of the user who is being followed by another user
    private Long followerUserId;
    // This is the id of the user who is following another user
    private Long followingUserId;

    public Long getFollowerUserId() {
        return followerUserId;
    }

    public void setFollowerUserId(Long followerUserId) {
        this.followerUserId = followerUserId;
    }

    public Long getFollowingUserId() {
        return followingUserId;
    }

    public void setFollowingUserId(Long followingUserId) {
        this.followingUserId = followingUserId;
    }

}
