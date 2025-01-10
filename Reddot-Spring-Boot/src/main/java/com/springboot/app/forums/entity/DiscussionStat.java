package com.springboot.app.forums.entity;

import com.springboot.app.model.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Entity
@Table(name = "discussion_stats")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DiscussionStat extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "last_comment_info_id", foreignKey = @ForeignKey(name = "fk_disc_stat_last_comment"))
    private CommentInfo lastComment; // last comment of this discussion

    @Column(name = "comment_count")
    private long commentCount;

    @Column(name = "view_count")
    private long viewCount;

    @Column(name = "last_viewed", columnDefinition = "DATETIME")
    private LocalDateTime lastViewed;

    @ElementCollection
    @CollectionTable(name = "disc_stat_commentor",
            joinColumns = {@JoinColumn(name = "disc_stat_id")})
    @MapKeyColumn(name = "commentor")
    @Column(name = "comment_count")
    private Map<String, Integer> commentors;

    public void addCommentCount(long number) {
        this.commentCount += number;
    }

    public void addViewCount(long number) {
        this.viewCount += number;
    }
}
