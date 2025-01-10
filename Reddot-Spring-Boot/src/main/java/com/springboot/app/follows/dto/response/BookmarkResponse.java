package com.springboot.app.follows.dto.response;

import java.time.LocalDateTime;

public class BookmarkResponse {
    private Long id;
    private String bookmarkBy;
    private boolean bookmarked;
    private LocalDateTime bookmarkedDate;

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

    public boolean isBookmarked() {
        return bookmarked;
    }

    public void setBookmarked(boolean bookmarked) {
        this.bookmarked = bookmarked;
    }

    public LocalDateTime getBookmarkedDate() {
        return bookmarkedDate;
    }

    public void setBookmarkedDate(LocalDateTime bookmarkedDate) {
        this.bookmarkedDate = bookmarkedDate;
    }

}
