package com.springboot.app.accounts.repository;

import com.springboot.app.accounts.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, PagingAndSortingRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    Optional<User> findByUsernameOrEmail(String username, String email);


    @Query("SELECT u FROM User u WHERE (:username IS NULL OR :username = '' OR u.username LIKE %:username%) OR (:email IS NULL OR :email = '' OR u.email LIKE %:email%)")
    Page<User> searchByUsernameOrEmail(@Param("username") String username, @Param("email") String email, Pageable pageable);

    @Query("SELECT u FROM User u WHERE (:username IS NULL OR :username = '' OR u.username LIKE %:username% OR u.name LIKE %:username%) AND u.username <> 'admin'")
    Page<User> searchByUsernameOrNameWithIgnoreAdmin(@Param("username") String username, Pageable pageable);

    @Query("SELECT u FROM User u WHERE (:username IS NULL OR :username = '' OR u.username LIKE %:username% OR u.name LIKE %:username%) AND u.username <> 'admin'")
    List<User> findAllUsersBy(@Param("username") String username);
}
