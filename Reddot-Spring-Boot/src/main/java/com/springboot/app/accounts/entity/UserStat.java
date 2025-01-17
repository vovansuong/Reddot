package com.springboot.app.accounts.entity;

import com.springboot.app.bagdes.Badge;
import com.springboot.app.forums.entity.CommentInfo;
import com.springboot.app.model.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
public class UserStat extends BaseEntity {
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "last_comment_info_id", foreignKey = @ForeignKey(name = "FK_USER_STAT_LAST_COMMENT_INFO"))
    private CommentInfo lastComment; // info about the last comment in the forum, use for display
    @Column(name = "comment_count")
    private long commentCount;
    @Column(name = "discussion_count")
    private long discussionCount; // number of discussions created by the user
    @Column(name = "reputation")
    private long reputation;
    @Column(name = "profile_viewed")
    private long profileViewed;
    @Column(name = "last_login")
    private LocalDateTime lastLogin;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "badge_id", foreignKey = @ForeignKey(name = "FK_USER_STAT_BADGE"))
    private Badge badge;
}
