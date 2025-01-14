package com.springboot.app.forums.repository;

import com.springboot.app.forums.entity.CommentInfo;
import com.springboot.app.forums.entity.Discussion;
import com.springboot.app.forums.entity.Forum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiscussionRepository extends JpaRepository<Discussion, Long> {
    @Modifying
    @Query("UPDATE Discussion d SET d.forum = :toForum WHERE d.forum = :fromForum")
    Integer moveDiscussion(Forum fromForum, Forum toForum);

    @Modifying
    @Query("SELECT d.stat.lastComment FROM Discussion d WHERE d.forum = :forum ORDER BY d.stat.lastComment.commentDate DESC")
    CommentInfo latestCommentInfo(Forum forum);

    // query Discussion by forumId
    @Query("SELECT d FROM Discussion d WHERE d.forum.id = :id")
    List<Discussion> findDiscussionByForumId(@Param("id") Long id);

    @Query("SELECT d FROM Discussion d WHERE (:title IS NULL OR :title = '' OR d.title LIKE %:title%)  AND (:forumId IS NULL OR d.forum.id = :forumId)")
    Page<Discussion> searchByTitle(@Param("title") String title, @Param("forumId") Long forumId, Pageable pageable);

    @Query("SELECT d FROM Discussion d JOIN d.tags t WHERE t.id = :tagId")
    List<Discussion> findDiscussionsByTagId(@Param("tagId") Long tagId);

    @Query("SELECT d FROM Discussion d WHERE (:title IS NULL OR :title = '' OR d.title LIKE %:title%)")
    List<Discussion> searchDiscussionByTitle(@Param("title") String title);
}