package com.springboot.app.bagdes;

import com.springboot.app.accounts.entity.User;
import com.springboot.app.accounts.repository.UserRepository;
import com.springboot.app.dto.response.AckCodeType;
import com.springboot.app.dto.response.ServiceResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class BadgeServiceImpl implements BadgeService {

    private static final Logger logger = LoggerFactory.getLogger(BadgeServiceImpl.class);

    @Autowired
    private BadgeRepository badgeRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public ServiceResponse<List<Badge>> getAllBadges() {
        ServiceResponse<List<Badge>> response = new ServiceResponse<>();
        List<Badge> badges = badgeRepository.findAll();
        response.setDataObject(badges);
        return response;
    }


    /*
     * Badges (tức là danh hiệu cho member, họ có thể dùng điểm để đổi lấy Badges.
     * Nếu họ ko đủ điểm để đổi lấy Badge, thì cần thực hiện các điều kiện tùy vào mỗi badge như sau:
     * - Beginner User - khi tạo tài khoản là có danh hiệu này (y/c: ko có)
     * - Trainee (30 point) (y/c: có ít nhất 2 lần tạo post + 2 comment trong bất kỳ discussion nào)
     * - Bronze User (100 point) (y/c: đã đăng hơn 10 post + ít nhất 1 lần tham gia comment)
     * - Silver User (500 point) (y/c: đã đăng >=100 post/comment)
     * - Gold User (800 point) (y/c: đã đăng >=500 post/comment)
     * - Platium User (1000 point) (y/c: đã đăng >=1000 post/comment)
     */

    // create badge default
    public ServiceResponse<Void> createBadge() {
        ServiceResponse<Void> response = new ServiceResponse<>();
        // add badge Beginner User
        Badge badgeBeginner = new Badge();
        badgeBeginner.setId(1L);
        badgeBeginner.setName("Beginner User");
        badgeBeginner.setDescription("This title will appear when you create an account");
        badgeBeginner.setIcon("fa-solid fa-shield-cat");
        badgeBeginner.setColor("#065535");
        badgeBeginner.setAction(true);
        badgeBeginner.setTotalScore(0L);
        badgeBeginner.setTotalDiscussion(0L);
        badgeBeginner.setTotalComment(0L);
        badgeRepository.save(badgeBeginner);
        logger.info("Create badge Beginner User success");
        return response;
    }

    public ServiceResponse<Void> createTraineeBadge() {
        ServiceResponse<Void> response = new ServiceResponse<>();
        // add badge Trainee
        Badge badgeTrainee = new Badge();
        badgeTrainee.setId(2L);
        badgeTrainee.setName("Trainee");
        badgeTrainee.setDescription("This badge is worth 30 POINTS / "
                                    + "Users need to complete the request: contribute to any discussion with AT LEAST 2 POSTS and 2 COMMENT");
        badgeTrainee.setIcon("fa-brands fa-napster");
        badgeTrainee.setColor("#2a623d");
        badgeTrainee.setAction(true);
        badgeTrainee.setTotalScore(30L);
        badgeTrainee.setTotalDiscussion(2L);
        badgeTrainee.setTotalComment(2L);
        badgeRepository.save(badgeTrainee);
        logger.info("Create badge Trainee success");
        return response;
    }

    public ServiceResponse<Void> createBronzeBadge() {
        ServiceResponse<Void> response = new ServiceResponse<>();
        // add badge Bronze User
        Badge badgeBronze = new Badge();
        badgeBronze.setId(3L);
        badgeBronze.setName("Bronze User");
        badgeBronze.setDescription("This badge is worth 100 POINTS / "
                                   + "Users need to complete the request: made more than 10 POSTS and AT LEAST 1 COMMENT to get it.");
        badgeBronze.setIcon("fa-solid fa-star-of-david");
        badgeBronze.setColor("#5d5d5d");
        badgeBronze.setAction(true);
        badgeBronze.setTotalScore(100L);
        badgeBronze.setTotalDiscussion(10L);
        badgeBronze.setTotalComment(1L);
        badgeRepository.save(badgeBronze);
        logger.info("Create badge Bronze User success");
        return response;
    }

    public ServiceResponse<Void> createSilverBadge() {
        ServiceResponse<Void> response = new ServiceResponse<>();
        // add badge Silver User
        Badge badgeSilver = new Badge();
        badgeSilver.setId(4L);
        badgeSilver.setName("Silver User");
        badgeSilver.setDescription("This badge is worth 500 POINTS / "
                                   + "Users need to complete the request: posted over 100 POSTS/COMMENTS to get it.");
        badgeSilver.setIcon("fa-regular fa-sun");
        badgeSilver.setColor("#aaaaaa");
        badgeSilver.setAction(true);
        badgeSilver.setTotalScore(500L);
        badgeSilver.setTotalDiscussion(100L);
        badgeSilver.setTotalComment(100L);
        badgeRepository.save(badgeSilver);
        logger.info("Create badge Silver User success");
        return response;
    }

    public ServiceResponse<Void> createGoldBadge() {
        ServiceResponse<Void> response = new ServiceResponse<>();
        // add badge Gold User
        Badge badgeGold = new Badge();
        badgeGold.setId(5L);
        badgeGold.setName("Gold User");
        badgeGold.setDescription("This badge is worth 800 POINTS / "
                                 + "Users need to complete the request: posted over 500 POSTS/COMMENTS to get it.");
        badgeGold.setIcon("fa-solid fa-sun");
        badgeGold.setColor("#a67c00");
        badgeGold.setAction(true);
        badgeGold.setTotalScore(800L);
        badgeGold.setTotalDiscussion(500L);
        badgeGold.setTotalComment(500L);

        badgeRepository.save(badgeGold);
        logger.info("Create badge Gold User success");
        return response;
    }

    public ServiceResponse<Void> createPlatinumBadge() {
        ServiceResponse<Void> response = new ServiceResponse<>();
        // add badge Platinum User
        Badge badgePlatinum = new Badge();
        badgePlatinum.setId(6L);
        badgePlatinum.setName("Platinum User");
        badgePlatinum.setDescription("This badge is worth 1,000 POINTS / "
                                     + "Users need to complete the request: posted over 1,000 POSTS/COMMENTS to get it.");
        badgePlatinum.setIcon("fa-solid fa-crown");
        badgePlatinum.setColor("#000000");
        badgePlatinum.setAction(true);
        badgePlatinum.setTotalScore(1000L);
        badgePlatinum.setTotalDiscussion(1000L);
        badgePlatinum.setTotalComment(1000L);
        badgeRepository.save(badgePlatinum);
        logger.info("Create badge Platinum User success");

        response.addMessage("Create badge success");
        return response;
    }

    // update badge
    @Override
    public ServiceResponse<Void> updateBadge(Long id, Badge badge) {
        ServiceResponse<Void> response = new ServiceResponse<>();
        Badge badgeExist = badgeRepository.findById(id).orElse(null);
        if (badgeExist == null) {
            response.setAckCode(AckCodeType.FAILURE);
            response.addMessage("Badge not found");
            return response;
        }
        badgeExist.setName(badge.getName());
        badgeExist.setDescription(badge.getDescription());
        badgeExist.setIcon(badge.getIcon());
        badgeExist.setColor(badge.getColor());
        badgeExist.setAction(badge.isAction());

        badgeRepository.save(badgeExist);
        response.setAckCode(AckCodeType.SUCCESS);
        response.addMessage("Update badge success");
        return response;
    }

    // Các điều kiện để đạt được badge
    // Update badge cho member
    @Override
    public ServiceResponse<Void> setBadgeForUser(User user) {
        ServiceResponse<Void> response = new ServiceResponse<>();
        String username = user.getUsername();
        long score = user.getStat().getReputation();
        long discussions = user.getStat().getDiscussionCount();
        long comments = user.getStat().getCommentCount();

        Badge badge = null;

        if (validatePlatinumBadge(score, discussions, comments) != null) {
            badge = validatePlatinumBadge(score, discussions, comments);
        } else if (validateGoldBadge(score, discussions, comments) != null) {
            badge = validateGoldBadge(score, discussions, comments);
        } else if (validateSilverBadge(score, discussions, comments) != null) {
            badge = validateSilverBadge(score, discussions, comments);
        } else if (validateBronzeBadge(score, discussions, comments) != null) {
            badge = validateBronzeBadge(score, discussions, comments);
        } else if (validateTrainingBadge(score, discussions, comments) != null) {
            badge = validateTrainingBadge(score, discussions, comments);
        } else {
            badge = badgeRepository.findById(1L).orElse(null);
        }

        if (badge == null) {
            response.setAckCode(AckCodeType.FAILURE);
            response.addMessage("Badge not found");
            return response;
        }

        user.getStat().setBadge(badge);
        userRepository.save(user);
        response.addMessage("Update badge success");
        return response;
    }

    @Override
    public ServiceResponse<Void> setBadgeForAllUser() {
        ServiceResponse<Void> response = new ServiceResponse<>();
        List<User> users = userRepository.findAll();
        for (User user : users) {
            setBadgeForUser(user);
        }
        response.addMessage("Update badge success");
        return response;
    }


    // Kiểm tra điều kiện để đạt được badge Trainee
    private Badge validateTrainingBadge(long score, long discussions, long comments) {
        Badge badge = badgeRepository.findById(2L).orElse(null);
        if (badge != null && discussions >= badge.getTotalDiscussion()
            && comments >= badge.getTotalComment()
            && score >= badge.getTotalScore()) {
            return badge;
        }
        return null;
    }

    // Kiểm tra điều kiện để đạt được badge Bronze User
    private Badge validateBronzeBadge(long score, long discussions, long comments) {
        Badge badge = badgeRepository.findById(3L).orElse(null);
        if (badge != null && discussions >= badge.getTotalDiscussion()
            && comments >= badge.getTotalComment()
            && score >= badge.getTotalScore()) {
            return badge;
        }
        return null;
    }

    // Kiểm tra điều kiện để đạt được badge Silver User
    private Badge validateSilverBadge(long score, long discussions, long comments) {
        Badge badge = badgeRepository.findById(4L).orElse(null);
        if (badge != null && discussions >= badge.getTotalDiscussion()
            && comments >= badge.getTotalComment()
            && score >= badge.getTotalScore()) {
            return badge;
        }
        return null;
    }

    // Kiểm tra điều kiện để đạt được badge Gold User
    private Badge validateGoldBadge(long score, long discussions, long comments) {
        Badge badge = badgeRepository.findById(5L).orElse(null);
        if (badge != null && score >= badge.getTotalScore()
            && (discussions >= badge.getTotalDiscussion()
                || comments >= badge.getTotalComment())
        ) {
            return badge;
        }
        return null;
    }

    // Kiểm tra điều kiện để đạt được badge Platinum User
    private Badge validatePlatinumBadge(long score, long discussions, long comments) {
        Badge badge = badgeRepository.findById(6L).orElse(null);
        if (badge != null && score >= badge.getTotalScore()
            && (discussions >= badge.getTotalDiscussion()
                || comments >= badge.getTotalComment())
        ) {
            return badge;
        }
        return null;
    }


}
