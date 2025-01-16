package com.springboot.app.accounts.enumeration;

public enum RoleName {
    ROLE_USER,
    ROLE_ADMIN,
    ROLE_MODERATOR;

    public String getAuthority() {
        return name();
    }
}
