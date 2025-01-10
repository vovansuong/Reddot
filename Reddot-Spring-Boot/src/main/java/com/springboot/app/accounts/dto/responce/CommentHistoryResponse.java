package com.springboot.app.accounts.dto.responce;

import com.springboot.app.forums.entity.Vote;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentHistoryResponse {
    private Long commentId;
    private String author; //username of the author

    private Long discussionId;
    private String discussionTitle;

    private String content;

    private LocalDateTime createdAt; //created date of the comment
    private LocalDateTime updatedAt; //updated date of the comment

    private Vote vote;
    private Long totalVotes; //total vote count of the comment

    private boolean isFirstComment; //if the comment is the first comment of the discussion
}
