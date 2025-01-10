package com.springboot.app.bagdes;

import com.springboot.app.accounts.entity.User;
import com.springboot.app.dto.response.ServiceResponse;

import java.util.List;

public interface BadgeService {
    ServiceResponse<List<Badge>> getAllBadges();

    ServiceResponse<Void> createBadge();

    ServiceResponse<Void> createTraineeBadge();

    ServiceResponse<Void> createBronzeBadge();

    ServiceResponse<Void> createSilverBadge();

    ServiceResponse<Void> createGoldBadge();

    ServiceResponse<Void> createPlatinumBadge();

    ServiceResponse<Void> updateBadge(Long id, Badge badge);

    ServiceResponse<Void> setBadgeForUser(User user);

    ServiceResponse<Void> setBadgeForAllUser();

}
