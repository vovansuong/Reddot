package com.springboot.app.forums.repository;

import com.springboot.app.forums.entity.CommentOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentOptionRepository extends JpaRepository<CommentOption, Long> {
}
