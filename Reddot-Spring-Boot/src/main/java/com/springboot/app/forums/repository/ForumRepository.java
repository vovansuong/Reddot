package com.springboot.app.forums.repository;

import com.springboot.app.forums.entity.Forum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("forumRepository")
public interface ForumRepository extends JpaRepository<Forum, Long> {
    @Query("SELECT f FROM Forum f WHERE f.forumGroup.id = :id")
    List<Forum> findForumByForumGroupId(@Param("id") Long id);

    @Query("SELECT MAX(fg.sortOrder) FROM ForumGroup fg")
    Integer findTopBySortOrder();

    // findTopbySortOrder by id for Forum
    @Query("SELECT MAX(fg.sortOrder) FROM Forum fg WHERE :id is null or fg.forumGroup.id = :id")
    Integer findTopBySortOrderForForum(@Param("id") Long id);

    @Modifying
    @Query("SELECT COUNT(f) FROM Forum f")
    Integer countForums();

    // find forum by title and forum active
    @Query("SELECT f FROM Forum f WHERE((:keyword IS NULL OR :keyword = '' OR f.title LIKE %:keyword%) OR (:keyword IS NULL OR :keyword = '' OR f.description LIKE %:keyword%)) AND f.active = true")
    List<Forum> findByTitle(@Param("keyword") String keyword);

    @Query("SELECT f FROM Forum f WHERE f.forumGroup.id = :id")
    List<Forum> findForumsByGroupId(@Param("id") Long groupId);
}
