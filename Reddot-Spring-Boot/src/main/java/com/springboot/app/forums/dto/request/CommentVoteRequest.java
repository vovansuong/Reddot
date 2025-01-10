package com.springboot.app.forums.dto.request;

public class CommentVoteRequest {
    private Long commentId;
    private String voteName;
    private Short voteValue;

    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }

    public String getVoteName() {
        return voteName;
    }

    public void setVoteName(String voteName) {
        this.voteName = voteName;
    }

    public Short getVoteValue() {
        return voteValue;
    }

    public void setVoteValue(Short voteValue) {
        this.voteValue = voteValue;
    }
}
