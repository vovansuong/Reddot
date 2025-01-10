package com.springboot.app.follows.entity;

import com.springboot.app.accounts.entity.User;
import com.springboot.app.follows.enumeration.FollowStatus;
import com.springboot.app.model.BaseEntity;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "follow_users")
public class FollowUser extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
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

    public FollowUser() {
        super();
    }

    public FollowUser(LocalDate followedDate, User followerUser, User followingUser, FollowStatus status) {
        super();
        this.followedDate = followedDate;
        this.followerUser = followerUser;
        this.followingUser = followingUser;
        this.status = status;
    }


    public FollowUser(Long id, LocalDate followedDate, User followerUser, User followingUser, FollowStatus status) {
        super();
        this.id = id;
        this.followedDate = followedDate;
        this.followerUser = followerUser;
        this.followingUser = followingUser;
        this.status = status;
    }

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        this.setCreatedAt(now);
        this.setUpdatedAt(now);
    }

    @PreUpdate
    public void preUpdate() {
        LocalDateTime now = LocalDateTime.now();
        this.setUpdatedAt(now);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getFollowedDate() {
        return followedDate;
    }

    public void setFollowedDate(LocalDate followedDate) {
        this.followedDate = followedDate;
    }

    public User getFollowerUser() {
        return followerUser;
    }

    public void setFollowerUser(User followerUser) {
        this.followerUser = followerUser;
    }

    public User getFollowingUser() {
        return followingUser;
    }

    public void setFollowingUser(User followingUser) {
        this.followingUser = followingUser;
    }

    public FollowStatus getStatus() {
        return status;
    }

    public void setStatus(FollowStatus status) {
        this.status = status;
    }

}
