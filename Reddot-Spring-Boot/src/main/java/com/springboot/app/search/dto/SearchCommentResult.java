package com.springboot.app.search.dto;

import com.springboot.app.forums.entity.Comment;

import java.util.List;

public class SearchCommentResult {
    // total # of hits for the search query
    private Long totalHits;

    // the actual comment return from the search query (just the current page)
    private List<Comment> comments;

    public Long getTotalHits() {
        return totalHits;
    }

    public void setTotalHits(Long totalHits) {
        this.totalHits = totalHits;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}
