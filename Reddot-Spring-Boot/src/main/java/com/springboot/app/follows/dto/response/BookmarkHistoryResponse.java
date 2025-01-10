package com.springboot.app.follows.dto.response;

import com.springboot.app.accounts.dto.responce.CommentHistoryResponse;

import java.time.LocalDateTime;

public class BookmarkHistoryResponse {

    private Long bookmarkId;
    private String bookmarkBy;
    private LocalDateTime bookmarkedDate;
    private String avatar; //avatar of the user who authored the comment
    private String imageUrl; //image url of the user who authored the comment

    private CommentHistoryResponse commentInfo;

    public Long getBookmarkId() {
        return bookmarkId;
    }

    public void setBookmarkId(Long bookmarkId) {
        this.bookmarkId = bookmarkId;
    }

    public String getBookmarkBy() {
        return bookmarkBy;
    }

    public void setBookmarkBy(String bookmarkBy) {
        this.bookmarkBy = bookmarkBy;
    }

    public LocalDateTime getBookmarkedDate() {
        return bookmarkedDate;
    }

    public void setBookmarkedDate(LocalDateTime bookmarkedDate) {
        this.bookmarkedDate = bookmarkedDate;
    }

    public CommentHistoryResponse getCommentInfo() {
        return commentInfo;
    }

    public void setCommentInfo(CommentHistoryResponse commentInfo) {
        this.commentInfo = commentInfo;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
