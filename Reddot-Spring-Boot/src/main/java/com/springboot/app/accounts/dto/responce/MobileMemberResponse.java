package com.springboot.app.accounts.dto.responce;

import com.springboot.app.accounts.enumeration.AccountStatus;
import lombok.Data;

@Data
public class MobileMemberResponse {
    private Long userId;
    private String username;
    private String email;
    private String name;
    private String imageUrl;
    private String avatar;
    private Long totalDiscussions;
    private Long totalComments;
    private Long reputation;
    private AccountStatus status;
}
