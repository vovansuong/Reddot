package com.springboot.app.forums.entity;

import com.springboot.app.model.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Entity(name = "forum_stats")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ForumStat extends BaseEntity {
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "last_comment_id", foreignKey = @ForeignKey(name = "FK_FORUM_STAT_LAST_COMMENT"))
    private CommentInfo lastComment; // info about the last comment in the forum, use for display

    @Column(name = "comment_count")
    private long commentCount;

    @Column(name = "discussion_count")
    private long discussionCount;
}
