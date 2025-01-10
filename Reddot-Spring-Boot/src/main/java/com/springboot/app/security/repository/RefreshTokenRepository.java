package com.springboot.app.security.repository;

import com.springboot.app.accounts.entity.User;
import com.springboot.app.security.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);

    Optional<RefreshToken> findByUserAndAvailable(User user, boolean available);

    List<RefreshToken> findByUser(User user);

    @Modifying
    int deleteByUser(User user);

    @Modifying
    int deleteByToken(String token);

    @Modifying
    int deleteByUserAndAvailable(User user, boolean available);
}
