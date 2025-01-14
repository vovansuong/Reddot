package com.springboot.app.forums.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.springboot.app.follows.entity.Bookmark;
import com.springboot.app.model.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
@Entity
@Table(name = "comments")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Comment extends BaseEntity {
    @Column(name = "title", length = 255)
    private String title;
    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "content", columnDefinition = "LONGTEXT")
    private String content; // content of the comment
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "discussion_id", foreignKey = @ForeignKey(name = "FK_COMMENT_DISCUSSION"))
    private Discussion discussion;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "reply_to_id", foreignKey = @ForeignKey(name = "FK_COMMENT_REPLY_TO"))
    private Comment replyTo; // parent of this comment, top level ones will have this field as null
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "replyTo", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("id ASC")
    private List<Comment> replies = new ArrayList<>(); // children of this comment
    @Column(name = "IP_address", length = 80)
    private String ipAddress;
    @Column(name = "hidden")
    private boolean hidden;
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "comment_vote_id", foreignKey = @ForeignKey(name = "FK_COMMENT_VOTE"))
    private CommentVote commentVote;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "comment", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("createdAt DESC")
    private List<Bookmark> bookmarks = new ArrayList<>();
}
