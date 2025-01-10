package com.springboot.app.forums.repository;

import com.springboot.app.forums.entity.DiscussionStat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DiscussionStatRepository extends JpaRepository<DiscussionStat, Long> {

    @Modifying
    @Query("DELETE FROM DiscussionStat ds WHERE ds.lastComment = :commentInfoId")
    void deleteByLastCommentInfoId(Long commentInfoId);

    @Modifying
    @Query("UPDATE DiscussionStat ds SET ds.lastComment = :newId WHERE ds.lastComment = :oldId")
    void updateLastCommentInfoId(Long oldId, Long newId);
}
