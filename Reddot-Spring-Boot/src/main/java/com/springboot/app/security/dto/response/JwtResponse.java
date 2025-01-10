package com.springboot.app.security.dto.response;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class JwtResponse {
    private String accessToken;
    private String type = "Bearer";
    private Long id;
    private String username;
    private String email;
    private Set<String> roles = new HashSet<String>();
    private String avatar;
    private String imageUrl;
    private String name;

    public JwtResponse() {
    }

    public JwtResponse(String accessToken, Long id, String username, String email, Set<String> roles) {
        this.accessToken = accessToken;
        this.id = id;
        this.username = username;
        this.email = email;
        this.roles = roles;
    }

    public JwtResponse(String accessToken, Long id, String username, String email, Set<String> roles, String avatar, String imageUrl, String name) {
        this.accessToken = accessToken;
        this.id = id;
        this.username = username;
        this.email = email;
        this.roles = roles;
        this.avatar = avatar;
        this.imageUrl = imageUrl;
        this.name = name;
    }

}