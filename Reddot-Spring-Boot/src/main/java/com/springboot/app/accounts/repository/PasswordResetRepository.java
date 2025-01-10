package com.springboot.app.accounts.repository;

import com.springboot.app.accounts.entity.PasswordReset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;

@Repository
public interface PasswordResetRepository extends JpaRepository<PasswordReset, Long> {
    @Modifying
    @Query("DELETE FROM PasswordReset pr WHERE pr.createdAt < :thresholdDate")
    Integer deleteLessThan(@Param("thresholdDate") Date thresholdDate);

    @Modifying
    @Query("Select count(pr) FROM PasswordReset pr WHERE pr.email = :email")
    Integer countEntities(@Param("email") String email);

    Boolean existsByEmail(String email);

    Optional<PasswordReset> findByResetKey(String resetKey);


}
