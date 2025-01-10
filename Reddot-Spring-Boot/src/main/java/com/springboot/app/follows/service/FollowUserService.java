package com.springboot.app.follows.service;

import com.springboot.app.dto.response.ServiceResponse;
import com.springboot.app.follows.dto.request.FollowUserRequest;
import com.springboot.app.follows.dto.response.FollowUserResponse;

import java.util.List;

public interface FollowUserService {
    ServiceResponse<Void> registerFollow(FollowUserRequest followUserRequest);

    ServiceResponse<Void> deleteFollowUser(Long id);

    ServiceResponse<List<FollowUserResponse>> getFollowUserByFollowerUsername(String username);

    ServiceResponse<List<FollowUserResponse>> getFollowUserByFollowingUsername(String username);

}
