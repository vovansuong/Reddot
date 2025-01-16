package com.springboot.app.forums.dto;

import com.springboot.app.forums.entity.Comment;
import lombok.Data;

@Data
public class AddCommentRequest {

    private Long discussionId;
    private Comment comment;
    private Long replyToId;
}
