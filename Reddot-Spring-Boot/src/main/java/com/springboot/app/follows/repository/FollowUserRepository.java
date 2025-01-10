package com.springboot.app.follows.repository;

import com.springboot.app.follows.entity.FollowUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FollowUserRepository extends JpaRepository<FollowUser, Long>, PagingAndSortingRepository<FollowUser, Long> {
    @Modifying
    @Query("DELETE FROM FollowUser f WHERE f.followerUser.id = :userId OR f.followingUser.id = :userId")
    void deleteBy(@Param("userId") Long userId);


    @Query("SELECT f FROM FollowUser f WHERE f.followerUser.id = :userId OR f.followingUser.id = :userId")
    List<FollowUser> findBy(@Param("userId") Long userId);

    boolean existsByFollowerUserIdAndFollowingUserId(Long followerUserId, Long followingUserId);

    Optional<FollowUser> findByFollowerUserIdAndFollowingUserId(Long followerUserId, Long followingUserId);

    List<FollowUser> findByFollowerUserId(Long userId);

    List<FollowUser> findByFollowingUserId(Long userId);

    //count followers by user id
    @Query("SELECT COUNT(f.followerUser.id) FROM FollowUser f WHERE f.followingUser.id = :userId")
    Long countFollowersByFollowingUserId(@Param("userId") Long userId);

    //count following by user id
    @Query("SELECT COUNT(f.followingUser.id) FROM FollowUser f WHERE f.followerUser.id = :userId")
    Long countFollowingByFollowerUserId(@Param("userId") Long userId);
}
