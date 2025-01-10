package com.springboot.app.forums.dto;

import com.springboot.app.forums.entity.Comment;

public class AddCommentRequest {

    private Long discussionId;
    private Comment comment;
    private Long replyToId;

    public Long getReplyToId() {
        return replyToId;
    }

    public void setReplyToId(Long replyToId) {
        this.replyToId = replyToId;
    }

    public Long getDiscussionId() {
        return discussionId;
    }

    public void setDiscussionId(Long discussionId) {
        this.discussionId = discussionId;
    }

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }
}
