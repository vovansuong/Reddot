package com.springboot.app.forums.repository;

import com.springboot.app.forums.entity.CommentInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentInfoRepository extends JpaRepository<CommentInfo, Long> {
    //find CommentInfo by commentId
    CommentInfo findByCommentId(Long commentId);
}
