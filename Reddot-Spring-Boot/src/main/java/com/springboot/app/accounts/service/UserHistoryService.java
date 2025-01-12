package com.springboot.app.accounts.service;

import com.springboot.app.accounts.dto.responce.CommentHistoryResponse;
import com.springboot.app.dto.response.PaginateResponse;
import com.springboot.app.forums.entity.Comment;
import org.springframework.stereotype.Component;

@Component
public interface UserHistoryService {

    PaginateResponse getAllBookmarksByUsername(int pageNo, int pageSize, String orderBy, String sortDir, String username);

    PaginateResponse getAllCommentsByUsername(int page, int size, String orderBy, String sortDirection, String username);

    CommentHistoryResponse mapCommentToCommentHistoryResponse(Comment comment);
}
