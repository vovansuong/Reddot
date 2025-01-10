package com.springboot.app.bannedKeyword;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BannedKeywordRepository extends JpaRepository<BannedKeyword, Long> {
    @Query("SELECT b FROM BannedKeyword b WHERE (:keyword IS NULL OR :keyword = '' OR b.keyword LIKE %:keyword%)")
    Page<BannedKeyword> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);
}

