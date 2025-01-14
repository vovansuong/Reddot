package com.springboot.app.follows.entity;

import com.springboot.app.forums.entity.Comment;
import com.springboot.app.model.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@Table(name = "bookmarks")
public class Bookmark extends BaseEntity {
    @Column(name = "bookmark_by", length = 50)
    private String bookmarkBy; //username of the user who bookmarked the comment
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id", foreignKey = @ForeignKey(name = "FK_BOOKMARK_COMMENT"))
    private Comment comment;
    @Column(name = "bookmarked")
    private Boolean bookmarked;
    @Column(name = "bookmarked_date", columnDefinition = "DATETIME")
    private LocalDateTime bookmarkedDate;
}
