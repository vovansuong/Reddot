package com.springboot.app.accounts.service.impl;

import com.springboot.app.accounts.dto.responce.CommentHistoryResponse;
import com.springboot.app.accounts.entity.User;
import com.springboot.app.accounts.repository.UserRepository;
import com.springboot.app.accounts.service.UserHistoryService;
import com.springboot.app.dto.response.PaginateResponse;
import com.springboot.app.follows.dto.response.BookmarkHistoryResponse;
import com.springboot.app.follows.entity.Bookmark;
import com.springboot.app.follows.repository.BookmarkRepository;
import com.springboot.app.forums.entity.Comment;
import com.springboot.app.forums.entity.CommentVote;
import com.springboot.app.forums.entity.Vote;
import com.springboot.app.forums.repository.CommentRepository;
import com.springboot.app.forums.service.VoteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class UserHistoryServiceImpl implements UserHistoryService {

    private static final Logger logger = LoggerFactory.getLogger(UserHistoryServiceImpl.class);

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private BookmarkRepository bookmarkRepository;

    @Autowired
    private VoteService voteService;
    @Autowired
    private UserRepository userRepository;

    @Override
    public PaginateResponse getAllBookmarksByUsername(int pageNo, int pageSize, String orderBy, String sortDir, String username) {
        logger.info("getAllBookmarksByUsername: username={}, pageNo={}, pageSize={}, orderBy={}, sortDir={}", username, pageNo, pageSize, orderBy, sortDir);
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(orderBy).ascending()
                : Sort.by(orderBy).descending();

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);

        Page<BookmarkHistoryResponse> itemsPage = bookmarkRepository.searchByUsername(username, pageable)
                .map(this::mapBookmarkToBookmarkHistoryResponse);

        return new PaginateResponse(
                itemsPage.getNumber() + 1,
                itemsPage.getSize(),
                itemsPage.getTotalPages(),
                itemsPage.getContent().size(),
                itemsPage.isLast(),
                itemsPage.getContent());
    }

    @Override
    public PaginateResponse getAllCommentsByUsername(int pageNo, int pageSize, String orderBy, String sortDir, String username) {
        logger.info("getAllCommentsByUsername: username={}, pageNo={}, pageSize={}, orderBy={}, sortDir={}", username, pageNo, pageSize, orderBy, sortDir);
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(orderBy).ascending()
                : Sort.by(orderBy).descending();

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);

        Page<CommentHistoryResponse> itemsPage = commentRepository.findAllByUsername(username, pageable)
                .map(this::mapCommentToCommentHistoryResponse);

        return new PaginateResponse(
                itemsPage.getNumber() + 1,
                itemsPage.getSize(),
                itemsPage.getTotalPages(),
                itemsPage.getContent().size(),
                itemsPage.isLast(),
                itemsPage.getContent());
    }

    private BookmarkHistoryResponse mapBookmarkToBookmarkHistoryResponse(Bookmark bookmark) {
        BookmarkHistoryResponse bookmarkHistoryResponse = new BookmarkHistoryResponse();
        bookmarkHistoryResponse.setBookmarkId(bookmark.getId());
        bookmarkHistoryResponse.setBookmarkBy(bookmark.getBookmarkBy());
        bookmarkHistoryResponse.setBookmarkedDate(bookmark.getBookmarkedDate());
        //avatar of author
        User user = userRepository.findByUsername(bookmark.getComment().getCreatedBy()).orElse(null);
        if (user != null) {
            bookmarkHistoryResponse.setAvatar(user.getAvatar());
            bookmarkHistoryResponse.setImageUrl(user.getImageUrl());
        }
        //comment info
        CommentHistoryResponse commentInfo = mapCommentToCommentHistoryResponse(bookmark.getComment());
        bookmarkHistoryResponse.setCommentInfo(commentInfo);
        return bookmarkHistoryResponse;
    }

    public CommentHistoryResponse mapCommentToCommentHistoryResponse(Comment comment) {
        CommentHistoryResponse commentHistoryResponse = new CommentHistoryResponse();
        commentHistoryResponse.setCommentId(comment.getId());
        commentHistoryResponse.setAuthor(comment.getCreatedBy());
        commentHistoryResponse.setDiscussionId(comment.getDiscussion().getId());
        commentHistoryResponse.setDiscussionTitle(comment.getDiscussion().getTitle());
        commentHistoryResponse.setContent(comment.getContent());
        commentHistoryResponse.setCreatedAt(comment.getCreatedAt());
        commentHistoryResponse.setUpdatedAt(comment.getUpdatedAt());

        CommentVote commentVote = comment.getCommentVote();
        if (commentVote != null && commentVote.getVotes() != null && !commentVote.getVotes().isEmpty()) {
            //get vote of current user on comment if any of them exists in the list of comments votes list
            Vote vote = voteService.getVote(comment.getCommentVote(), comment.getCreatedBy());
            commentHistoryResponse.setVote(vote);
            //get total votes on comment
            Long totalVotes = commentVote.getVotes().stream().mapToLong(Vote::getVoteValue).sum();
            commentHistoryResponse.setTotalVotes(totalVotes);
        }
        //check if first comment of discussion
        Comment firstComment = commentRepository.findFirstCommentByDiscussion(comment.getDiscussion());
        commentHistoryResponse.setFirstComment(firstComment != null && firstComment.getId().equals(comment.getId()));

        return commentHistoryResponse;
    }

}
