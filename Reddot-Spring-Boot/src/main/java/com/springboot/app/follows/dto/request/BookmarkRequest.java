package com.springboot.app.follows.dto.request;

public class BookmarkRequest {
    private Long commentId;
    private String bookmarkBy;
    private Boolean bookmarked;

    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }

    public String getBookmarkBy() {
        return bookmarkBy;
    }

    public void setBookmarkBy(String bookmarkBy) {
        this.bookmarkBy = bookmarkBy;
    }

    public Boolean getBookmarked() {
        return bookmarked;
    }

    public void setBookmarked(Boolean bookmarked) {
        this.bookmarked = bookmarked;
    }
}
