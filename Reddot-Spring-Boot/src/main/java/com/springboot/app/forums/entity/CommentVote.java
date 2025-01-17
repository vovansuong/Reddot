package com.springboot.app.forums.entity;

import com.springboot.app.model.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Entity
@Table(name = "comment_votes")
public class CommentVote extends BaseEntity {
    @Column(name = "vote_up_count")
    private int voteUpCount;

    @Column(name = "vote_down_count")
    private int voteDownCount;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(
            name = "comment_vote_id",
            joinColumns = {@JoinColumn(name = "comment_vote_id", foreignKey = @ForeignKey(name = "FK_COMMENT_VOTE_COMMENT_ID"))},
            inverseJoinColumns = {@JoinColumn(name = "vote_id", foreignKey = @ForeignKey(name = "FK_COMMENT_VOTE_VOTE_ID"))},
            indexes = {@Index(name = "IDX_COMMENT_VOTE_VOTE", columnList = "COMMENT_VOTE_ID,VOTE_ID")})
    private Set<Vote> votes;


}
