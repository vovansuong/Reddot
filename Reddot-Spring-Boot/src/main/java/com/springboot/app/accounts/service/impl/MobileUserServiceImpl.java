package com.springboot.app.accounts.service.impl;

import com.springboot.app.accounts.dto.request.AccountInfo;
import com.springboot.app.accounts.dto.responce.CommentHistoryResponse;
import com.springboot.app.accounts.dto.responce.MobileMemberResponse;
import com.springboot.app.accounts.dto.responce.MobileUserInfoResponse;
import com.springboot.app.accounts.entity.Person;
import com.springboot.app.accounts.entity.User;
import com.springboot.app.accounts.enumeration.Gender;
import com.springboot.app.accounts.repository.UserRepository;
import com.springboot.app.accounts.service.MobileUserService;
import com.springboot.app.accounts.service.UserHistoryService;
import com.springboot.app.dto.response.AckCodeType;
import com.springboot.app.dto.response.ServiceResponse;
import com.springboot.app.follows.dto.response.FollowUserResponse;
import com.springboot.app.follows.service.FollowUserService;
import com.springboot.app.forums.entity.Comment;
import com.springboot.app.forums.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class MobileUserServiceImpl implements MobileUserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FollowUserService followUserService;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserHistoryService userHistoryService;

    @Override
    public ServiceResponse<List<MobileMemberResponse>> getAllMembers() {
        ServiceResponse<List<MobileMemberResponse>> response = new ServiceResponse<>();
        List<User> users = userRepository.findAll();
        List<MobileMemberResponse> members = users.stream().map(this::toMobileMemberResponse).toList();
        response.setDataObject(members);
        return response;
    }

    @Override
    public ServiceResponse<List<MobileMemberResponse>> getMembersBy(String search) {
        ServiceResponse<List<MobileMemberResponse>> response = new ServiceResponse<>();

        List<User> users = userRepository.findAllUsersBy(search);

        List<MobileMemberResponse> members = users.stream().map(this::toMobileMemberResponse).toList();
        response.setDataObject(members);
        return response;
    }

    @Override
    public ServiceResponse<MobileUserInfoResponse> getMemberByUsername(String username) {
        ServiceResponse<MobileUserInfoResponse> response = new ServiceResponse<>();
        User user = userRepository.findByUsername(username).orElse(null);
        if (user == null) {
            response.addMessage("User not found");
            return response;
        }
        MobileUserInfoResponse userInfo = toMobileUserInfoResponse(user);
        response.setDataObject(userInfo);
        return response;
    }

    @Override
    public ServiceResponse<MobileUserInfoResponse> updateMemberByUsername(AccountInfo userInfo) {
        ServiceResponse<MobileUserInfoResponse> response = new ServiceResponse<>();
        User user = userRepository.findByUsername(userInfo.getUsername()).orElse(null);
        if (user == null) {
            response.addMessage("User not found");
            response.setAckCode(AckCodeType.FAILURE);
            return response;
        }
        List<String> errors = validatePersonForUpdate(userInfo);
        if (!errors.isEmpty()) {
            response.setAckCode(AckCodeType.FAILURE);
            response.setMessages(errors);
            return response;
        }

        user.setName(userInfo.getName());
        Person person = user.getPerson();
        person.setAddress(userInfo.getAddress());
        person.setBio(userInfo.getBio());

        person.setBirthDate(userInfo.getBirthday());
        person.setGender(convertGender(userInfo.getGender()));
        return response;
    }

    private Gender convertGender(String gender) {
        if ("MALE".equals(gender)) {
            return Gender.MALE;
        }
        if ("FEMALE".equals(gender)) {
            return Gender.FEMALE;
        }
        return Gender.OTHER;
    }


    private List<String> validatePersonForUpdate(AccountInfo accountInfo) {
        List<String> errors = new ArrayList<>();
        if (accountInfo == null) {
            errors.add("Person is not found");
            return errors;
        }
        if ("".equals(accountInfo.getName())) {
            errors.add("Full Name must not be empty");
        }
        if ("".equals(accountInfo.getPhone())) {
            errors.add("Phone number must not be empty");
        }
        if ("".equals(accountInfo.getAddress())) {
            errors.add("Address must not be empty");
        }
        //check if birthday must be over 18 years old
        if (accountInfo.getBirthday() != null) {
            LocalDate now = LocalDate.now();
            int age = now.getYear() - accountInfo.getBirthday().getYear();
            if (age < 18) {
                errors.add("Birthday must be over 18 years old");
            }
        }
        return errors;
    }

    private MobileUserInfoResponse toMobileUserInfoResponse(User user) {
        MobileUserInfoResponse userInfo = new MobileUserInfoResponse();
        userInfo.setUserId(user.getId());
        userInfo.setUsername(user.getUsername());
        userInfo.setName(user.getName());
        userInfo.setAvatar(user.getAvatar());
        userInfo.setImageUrl(user.getImageUrl());

        userInfo.setPhone(user.getPerson().getPhone());
        userInfo.setAddress(user.getPerson().getAddress());
        userInfo.setStatus(user.getAccountStatus());

        userInfo.setBio(user.getPerson().getBio());
        userInfo.setBirthDate(user.getPerson().getBirthDate());
        userInfo.setGender(String.valueOf(user.getPerson().getGender()));

        userInfo.setTotalDiscussions(user.getStat().getDiscussionCount());
        userInfo.setTotalComments(user.getStat().getCommentCount());
        userInfo.setReputation(user.getStat().getReputation());

        ServiceResponse<List<FollowUserResponse>> listFollow = followUserService.getFollowUserByFollowerUsername(user.getUsername());
        long totalFollowers = listFollow.getDataObject().size();
        userInfo.setTotalFollowers(totalFollowers);

        ServiceResponse<List<FollowUserResponse>> listFollowing = followUserService.getFollowUserByFollowingUsername(user.getUsername());
        long totalFollowing = listFollowing.getDataObject().size();
        userInfo.setTotalFollowing(totalFollowing);

        //list comments by username
        List<Comment> comments = commentRepository.findByCreatedBy(user.getUsername());
        if (comments != null && !comments.isEmpty()) {
            List<CommentHistoryResponse> commentHistoryResponses = comments.stream()
                    .map(item -> userHistoryService.mapCommentToCommentHistoryResponse(item)).toList();
            userInfo.setComments(commentHistoryResponses);
        } else {
            userInfo.setComments(new ArrayList<>());
        }
        return userInfo;
    }


    private MobileMemberResponse toMobileMemberResponse(User user) {
        MobileMemberResponse member = new MobileMemberResponse();
        member.setUserId(user.getId());
        member.setUsername(user.getUsername());
        member.setName(user.getName());
        member.setAvatar(user.getAvatar());
        member.setImageUrl(user.getImageUrl());
        member.setStatus(user.getAccountStatus());
        member.setTotalDiscussions(user.getStat().getDiscussionCount());
        member.setTotalComments(user.getStat().getCommentCount());
        member.setReputation(user.getStat().getReputation());
        return member;
    }

}
