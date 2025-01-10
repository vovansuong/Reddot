package com.springboot.app.search.dto;

import com.springboot.app.forums.entity.Discussion;

import java.util.List;

public class SearchDiscussionResult {

    // total # of hits for the search query
    private Long totalHits;

    // the actual discussions return from the search query (just the current page)
    private List<Discussion> discussions;

    public Long getTotalHits() {
        return totalHits;
    }

    public void setTotalHits(Long totalHits) {
        this.totalHits = totalHits;
    }

    public List<Discussion> getDiscussions() {
        return discussions;
    }

    public void setDiscussions(List<Discussion> discussions) {
        this.discussions = discussions;
    }
}