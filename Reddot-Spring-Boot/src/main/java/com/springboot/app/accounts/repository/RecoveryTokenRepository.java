package com.springboot.app.accounts.repository;

import com.springboot.app.accounts.entity.RecoveryToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;

@Repository
public interface RecoveryTokenRepository extends JpaRepository<RecoveryToken, Long> {
    @Modifying
    @Query("DELETE FROM RecoveryToken rt WHERE rt.createdAt < :thresholdDate")
    Integer deleteLessThan(@Param("thresholdDate") Date thresholdDate);

    @Modifying
    @Query("Select count(rt) FROM RecoveryToken rt WHERE rt.email = :email")
    Integer countEntities(@Param("email") String email);

    Boolean existsByEmail(String email);

    Optional<RecoveryToken> findByResetKey(String resetKey);
}
