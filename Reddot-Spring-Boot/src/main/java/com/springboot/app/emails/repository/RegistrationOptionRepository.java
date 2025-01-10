package com.springboot.app.emails.repository;

import com.springboot.app.emails.entity.RegistrationOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegistrationOptionRepository extends JpaRepository<RegistrationOption, Long> {
}
