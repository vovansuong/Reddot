package com.springboot.app.follows.entity;

import com.springboot.app.forums.entity.Comment;
import com.springboot.app.model.BaseEntity;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "bookmarks")
public class Bookmark extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "bookmark_by", length = 50)
    private String bookmarkBy; //username of the user who bookmarked the comment
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id", foreignKey = @ForeignKey(name = "FK_BOOKMARK_COMMENT"))
    private Comment comment;
    @Column(name = "bookmarked")
    private Boolean bookmarked;
    @Column(name = "bookmarked_date", columnDefinition = "DATETIME")
    private LocalDateTime bookmarkedDate;

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

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBookmarkBy() {
        return bookmarkBy;
    }

    public void setBookmarkBy(String bookmarkBy) {
        this.bookmarkBy = bookmarkBy;
    }

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

    public Boolean getBookmarked() {
        return bookmarked;
    }

    public void setBookmarked(Boolean bookmarked) {
        this.bookmarked = bookmarked;
    }

    public LocalDateTime getBookmarkedDate() {
        return bookmarkedDate;
    }

    public void setBookmarkedDate(LocalDateTime bookmarkedDate) {
        this.bookmarkedDate = bookmarkedDate;
    }
}
