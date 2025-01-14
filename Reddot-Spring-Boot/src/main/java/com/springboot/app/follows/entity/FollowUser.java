package com.springboot.app.follows.entity;

import com.springboot.app.accounts.entity.User;
import com.springboot.app.follows.enumeration.FollowStatus;
import com.springboot.app.model.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "follow_users")
public class FollowUser extends BaseEntity {
    @Column(name = "followed_date", columnDefinition = "DATE")
    private LocalDate followedDate;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "follower_user_id", foreignKey = @ForeignKey(name = "fk_follow_user_follower"))
    private User followerUser;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "following_user_id", foreignKey = @ForeignKey(name = "fk_follow_user_following"))
    private User followingUser;
    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 50)
    private FollowStatus status;
}
