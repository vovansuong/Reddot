package com.springboot.app.follows.service;

import com.springboot.app.dto.response.AckCodeType;
import com.springboot.app.dto.response.ServiceResponse;
import com.springboot.app.follows.dto.request.BookmarkRequest;
import com.springboot.app.follows.entity.Bookmark;
import com.springboot.app.follows.repository.BookmarkRepository;
import com.springboot.app.forums.entity.Comment;
import com.springboot.app.forums.repository.CommentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class BookmarkServiceImpl implements BookmarkService {

    private static final Logger logger = LoggerFactory.getLogger(BookmarkServiceImpl.class);

    @Autowired
    private BookmarkRepository bookmarkRepository;

    @Autowired
    private CommentRepository commentRepository;


    @Override
    public ServiceResponse<Void> registerBookmark(BookmarkRequest bookmarkRequest) {
        ServiceResponse<Void> response = new ServiceResponse<>();
        List<String> errors = validateBookmark(bookmarkRequest);
        if (!errors.isEmpty()) {
            response.addMessages(errors);
            response.setAckCode(AckCodeType.FAILURE);
            return response;
        }
        Comment comment = commentRepository.findById(bookmarkRequest.getCommentId()).orElse(null);
        if (comment == null) {
            logger.info("Comment not found");
            response.addMessage("Comment not found");
            response.setAckCode(AckCodeType.FAILURE);
            return response;
        }
        if (comment.getCreatedBy().equals(bookmarkRequest.getBookmarkBy())) {
            logger.info("User cannot bookmark own comment");
            response.addMessage("User cannot bookmark own comment");
            response.setAckCode(AckCodeType.FAILURE);
            return response;
        }
        List<Bookmark> bookmarks = comment.getBookmarks();
        Bookmark bookmark = bookmarks.stream()
                .filter(b -> b.getBookmarkBy()
                        .equals(bookmarkRequest.getBookmarkBy())).findFirst()
                .orElse(null);

        if (bookmark == null) {
            //create new bookmark
            Bookmark newBookmark = new Bookmark();
            newBookmark.setComment(comment);
            newBookmark.setBookmarkBy(bookmarkRequest.getBookmarkBy());
            newBookmark.setBookmarked(true);
            newBookmark.setBookmarkedDate(LocalDateTime.now());
            newBookmark.setCreatedBy(bookmarkRequest.getBookmarkBy());
            bookmarkRepository.save(newBookmark);
            logger.info("Bookmark created successfully");
            response.addMessage("Bookmark created successfully");
        } else {
            //delete bookmark
            comment.getBookmarks().remove(bookmark);
            commentRepository.save(comment);
            bookmarkRepository.delete(bookmark);
            logger.info("Bookmark deleted successfully");
            response.addMessage("Bookmark deleted successfully");
        }
        return response;
    }

    private List<String> validateBookmark(BookmarkRequest bookmarkRequest) {
        List<String> errors = new ArrayList<>();
        if (bookmarkRequest.getCommentId() == null) {
            errors.add("Comment id is required");
        }
        if (bookmarkRequest.getBookmarkBy() == null) {
            errors.add("Bookmark by is required");
        }

        return errors;
    }

}
