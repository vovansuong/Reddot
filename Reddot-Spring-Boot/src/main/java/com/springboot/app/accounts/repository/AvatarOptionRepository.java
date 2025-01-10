package com.springboot.app.accounts.repository;

import com.springboot.app.accounts.entity.AvatarOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AvatarOptionRepository extends JpaRepository<AvatarOption, Long> {
}
