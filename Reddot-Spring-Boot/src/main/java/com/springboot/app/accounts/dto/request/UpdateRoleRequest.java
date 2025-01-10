package com.springboot.app.accounts.dto.request;

import java.util.Set;

public class UpdateRoleRequest {
    private Long userId;
    private Set<String> roles;


    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }
}
