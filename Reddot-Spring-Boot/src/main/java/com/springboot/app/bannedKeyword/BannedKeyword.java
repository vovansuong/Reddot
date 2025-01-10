package com.springboot.app.bannedKeyword;

import com.springboot.app.model.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "banned_keywords")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BannedKeyword extends BaseEntity {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;
    @Column(name = "keyword", length = 100)
    private String keyword;

    @PrePersist
    public void prePersist() {
        this.setCreatedAt(java.time.LocalDateTime.now());
        this.setUpdatedAt(java.time.LocalDateTime.now());
    }

    @PreUpdate
    public void preUpdate() {
        this.setUpdatedAt(java.time.LocalDateTime.now());
    }
}
