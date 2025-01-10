package com.springboot.app.follows.dto.response;

import com.springboot.app.accounts.enumeration.AccountStatus;
import lombok.Data;

@Data
public class FollowUserResponse {
    private Long userId;
    private String username;
    private String name;
    private String avatar;
    private String imageUrl;

    private Long totalFollowers;
    private Long totalFollowing;

    private AccountStatus accountStatus;


}
