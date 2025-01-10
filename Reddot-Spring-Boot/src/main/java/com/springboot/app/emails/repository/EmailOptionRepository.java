package com.springboot.app.emails.repository;

import com.springboot.app.emails.entity.EmailOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailOptionRepository extends JpaRepository<EmailOption, Long> {
}
