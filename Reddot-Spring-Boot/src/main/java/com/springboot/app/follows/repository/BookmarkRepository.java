package com.springboot.app.follows.repository;

import com.springboot.app.follows.entity.Bookmark;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark, Long>, PagingAndSortingRepository<Bookmark, Long> {

    @Query("SELECT u FROM Bookmark u WHERE (:username IS NULL OR :username = '' OR u.bookmarkBy LIKE :username)")
    Page<Bookmark> searchByUsername(@Param("username") String username, Pageable pageable);

}
