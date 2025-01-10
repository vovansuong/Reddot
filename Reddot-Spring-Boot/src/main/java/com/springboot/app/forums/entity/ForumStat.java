package com.springboot.app.forums.entity;

import com.springboot.app.model.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "forum_stats")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ForumStat extends BaseEntity {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "last_comment_id", foreignKey = @ForeignKey(name = "FK_FORUM_STAT_LAST_COMMENT"))
    private CommentInfo lastComment; // info about the last comment in the forum, use for display

    @Column(name = "comment_count")
    private long commentCount;

    @Column(name = "discussion_count")
    private long discussionCount;

    public void addCommentCount(long number) {
        this.commentCount += number;
    }

    public void addDiscussionCount(long number) {
        this.discussionCount += number;
    }
}
