package com.springboot.app.accounts.repository;

import com.springboot.app.accounts.entity.UserStat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserStatRepository extends JpaRepository<UserStat, Long>, PagingAndSortingRepository<UserStat, Long> {

    @Query("SELECT u FROM UserStat u WHERE (:username IS NULL OR :username = '' OR u.createdBy LIKE %:username%)")
    Page<UserStat> searchByUsername(@Param("username") String username, Pageable pageable);

    @Query("SELECT u FROM UserStat u WHERE (:username IS NULL OR :username = '' OR u.createdBy LIKE %:username%) AND u.createdBy <> 'admin'")
    Page<UserStat> findAllByUsernameWithIgnoreAdmin(@Param("username") String username, Pageable pageable);

}
