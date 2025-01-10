package com.springboot.app.forums.service;

import com.springboot.app.dto.response.PaginateResponse;
import com.springboot.app.dto.response.ServiceResponse;
import com.springboot.app.forums.dto.CommentDTO;
import com.springboot.app.forums.dto.response.DiscussionResponse;
import com.springboot.app.forums.dto.response.ViewCommentResponse;
import com.springboot.app.forums.dto.search.SearchAll;
import com.springboot.app.forums.entity.Comment;

import java.util.List;

public interface CommentService {
    PaginateResponse getAllCommentsByDiscussionId(int pageNo, int pageSize, String orderBy, String sortDir,
                                                  Long discussionId);

    ServiceResponse<DiscussionResponse> getFirstCommentByDiscussionId(Long discussionId);

    ServiceResponse<Comment> addComment(Long discussionId, Comment comment, String username, Long replyToId);

    Comment updateComment(Comment comment);

    ServiceResponse<List<CommentDTO>> getAllComment();

    ServiceResponse<List<SearchAll>> getSearchComments(String keyword);

    ViewCommentResponse mapCommentToViewCommentResponse(Comment comment);

    String getContentByCommentId(Long id);

    ServiceResponse<Comment> deleteComment(Long id, Long discussionId);
}
