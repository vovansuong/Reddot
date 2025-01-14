package com.springboot.app.accounts.entity;

import com.springboot.app.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@RequiredArgsConstructor
@NoArgsConstructor
public class RecoveryToken extends BaseEntity {
    @NonNull
    @Column(name = "owner_id")
    private Long ownerId;

    @Column(name = "email", length = 128)
    private String email;

    @Column(name = "reset_key", length = 64)
    private String resetKey = UUID.randomUUID().toString();

    private boolean used = false;

    @Column(name = "valid_before")
    private LocalDateTime validBefore = LocalDateTime.now().plusMinutes(30);
}
