package com.springboot.app.follows.service;

import com.springboot.app.accounts.entity.User;
import com.springboot.app.accounts.repository.UserRepository;
import com.springboot.app.dto.response.AckCodeType;
import com.springboot.app.dto.response.ServiceResponse;
import com.springboot.app.follows.dto.request.FollowUserRequest;
import com.springboot.app.follows.dto.response.FollowUserResponse;
import com.springboot.app.follows.entity.FollowUser;
import com.springboot.app.follows.repository.FollowUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class FollowUserServiceImpl implements FollowUserService {
    private static final Logger logger = LoggerFactory.getLogger(FollowUserServiceImpl.class);

    @Autowired
    private FollowUserRepository followUserRepository;

    @Autowired
    private UserRepository userRepository;


    @Override
    public ServiceResponse<Void> registerFollow(FollowUserRequest followUserRequest) {
        ServiceResponse<Void> response = new ServiceResponse<>();
        //check if user exists
        Optional<User> followerUserExist = userRepository.findById(followUserRequest.getFollowerUserId());
        Optional<User> followingUserExist = userRepository.findById(followUserRequest.getFollowingUserId());
        if (followerUserExist.isEmpty() || followingUserExist.isEmpty()) {
            logger.error("User does not exist");
            response.setAckCode(AckCodeType.FAILURE);
            response.addMessage("User does not exist");
            return response;
        }

        FollowUser followUser = followUserRepository
                .findByFollowerUserIdAndFollowingUserId(followUserRequest.getFollowerUserId(), followUserRequest.getFollowingUserId())
                .orElse(null);

        if (followUser == null) {
            followUser = new FollowUser();
            followUser.setFollowingUser(followingUserExist.get());
            followUser.setFollowerUser(followerUserExist.get());
            followUser.setFollowedDate(LocalDate.now());
            //save follow user
            followUserRepository.save(followUser);
            response.addMessage("Follow user");
            logger.info("Follow user");
        } else {
            //delete follow user
            followUserRepository.delete(followUser);
            response.addMessage("Unfollow user");
            logger.info("Unfollow user");
        }

        return response;
    }

    @Override
    public ServiceResponse<Void> deleteFollowUser(Long id) {
        ServiceResponse<Void> response = new ServiceResponse<>();
        followUserRepository.deleteById(id);
        response.addMessage("Follow user deleted");
        return response;
    }

    @Override
    public ServiceResponse<List<FollowUserResponse>> getFollowUserByFollowerUsername(String username) {
        ServiceResponse<List<FollowUserResponse>> response = new ServiceResponse<>();
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            logger.error("User does not exist");
            response.setAckCode(AckCodeType.FAILURE);
            response.addMessage("User does not exist");
            return response;
        }
        Long userId = user.get().getId();

        List<FollowUserResponse> followUserList = followUserRepository.findByFollowerUserId(userId).stream()
                .map(this::convertToFollowingUser)
                .toList();
        response.addMessage("Get follow user list");
        response.setDataObject(followUserList);
        logger.info("Get follow user list");
        return response;
    }

    @Override
    public ServiceResponse<List<FollowUserResponse>> getFollowUserByFollowingUsername(String username) {
        ServiceResponse<List<FollowUserResponse>> response = new ServiceResponse<>();
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            logger.error("User does not exist");
            response.setAckCode(AckCodeType.FAILURE);
            response.addMessage("User does not exist");
            return response;
        }
        Long userId = user.get().getId();
        List<FollowUserResponse> followUserList = followUserRepository.findByFollowingUserId(userId).stream()
                .map(this::convertToFollowerUser)
                .toList();
        response.addMessage("Get follow user list");
        response.setDataObject(followUserList);
        logger.info("Get follow user list");
        return response;
    }

    private FollowUserResponse convertToFollowerUser(FollowUser followUser) {
        return convertToFollowUserResponse(followUser.getFollowerUser());
    }

    private FollowUserResponse convertToFollowingUser(FollowUser followUser) {
        return convertToFollowUserResponse(followUser.getFollowingUser());
    }

    private FollowUserResponse convertToFollowUserResponse(User user) {
        FollowUserResponse followUserResponse = new FollowUserResponse();
        followUserResponse.setUserId(user.getId());
        followUserResponse.setUsername(user.getUsername());
        followUserResponse.setName(user.getName());
        followUserResponse.setImageUrl(user.getImageUrl());
        followUserResponse.setAvatar(user.getAvatar());

        Long countFollowers = followUserRepository.countFollowingByFollowerUserId(user.getId());
        Long countFollowing = followUserRepository.countFollowersByFollowingUserId(user.getId());
        followUserResponse.setTotalFollowers(countFollowers);
        followUserResponse.setTotalFollowing(countFollowing);
        return followUserResponse;
    }
}
