package com.springboot.app.accounts.entity;

import com.springboot.app.model.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "avatar_option")
public class AvatarOption extends BaseEntity {
    @Id
    private Long id;
    @Column(name = "max_file_size")
    private int maxFileSize;
    @Column(name = "max_width")
    private int maxWidth;
    @Column(name = "max_height")
    private int maxHeight;

    @PrePersist
    public void prePersist() {
        this.setCreatedAt(LocalDateTime.now());
    }

    @PreUpdate
    public void preUpdate() {
        this.setUpdatedAt(LocalDateTime.now());
    }
}
