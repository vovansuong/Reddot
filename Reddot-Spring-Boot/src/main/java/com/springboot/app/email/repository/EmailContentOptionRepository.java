package com.springboot.app.email.repository;

import com.springboot.app.email.entity.EmailContentOption;
import com.springboot.app.email.entity.EmailOptionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailContentOptionRepository extends JpaRepository<EmailContentOption, Long> {
    EmailContentOption findByOptionType(EmailOptionType optionType);
}
