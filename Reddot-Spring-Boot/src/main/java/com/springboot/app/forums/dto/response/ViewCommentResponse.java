package com.springboot.app.forums.dto.response;

import com.springboot.app.follows.dto.response.BookmarkResponse;
import com.springboot.app.forums.entity.Vote;
import com.springboot.app.tags.Tag;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
public class ViewCommentResponse {
    private Long commentId;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    //author
    private Author author;

    //discussion
    private Long discussionId;
    private boolean isFirstComment;
    private List<Tag> tags;
    private boolean closed;
    //comment
    private String title;
    private String content;
    private boolean hidden;
    private List<ReplyItem> replies;

    //votes
    private Long totalVotes; // vote up count - vote down count
    private Set<Vote> votes; //list of votes for this comment
    //bookmark
    private List<BookmarkResponse> bookmarks; //list of bookmarks for this comment

    // accepted answer
    private boolean acceptedAnswer;

}

