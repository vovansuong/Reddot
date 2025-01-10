package com.springboot.app.accounts.dto.responce;

import com.springboot.app.accounts.enumeration.AccountStatus;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class MobileUserInfoResponse {
    private Long userId;
    private String username;
    private String email;
    private String name;
    private String phone;
    private String imageUrl;
    private String avatar;
    private AccountStatus status;
    private String address;
    private String bio;
    private LocalDate birthDate;
    private String gender;

    private Long totalDiscussions;
    private Long totalComments;
    private Long reputation;

    private Long totalFollowers;
    private Long totalFollowing;

    private List<CommentHistoryResponse> comments;

}
