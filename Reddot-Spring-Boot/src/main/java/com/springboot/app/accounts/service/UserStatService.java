package com.springboot.app.accounts.service;

import com.springboot.app.accounts.entity.UserStat;
import com.springboot.app.dto.response.PaginateResponse;
import com.springboot.app.dto.response.ServiceResponse;
import org.springframework.stereotype.Component;

@Component
public interface UserStatService {
    PaginateResponse getAllUserStats(int page, int size, String orderBy, String sortDirection, String search);

    PaginateResponse getAllUserStatsWithIgnoreAdmin(int page, int size, String orderBy, String sortDirection, String search);

    ServiceResponse<UserStat> updateProfileViewed(String username);

    ServiceResponse<UserStat> syncUserStat(String username);
}

