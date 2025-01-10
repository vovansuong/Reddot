package com.springboot.app.emails.entity;

import com.springboot.app.model.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "email_option")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailOption extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "host", length = 80)
    private String host;
    @Column(name = "port")
    private Integer port;
    @Column(name = "username", length = 80)
    private String username;
    @Column(name = "password", length = 80)
    private String password;
    @Column(name = "tls_enable")
    private Boolean tlsEnable;
    @Column(name = "authentication")
    private Boolean authentication;

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        this.setCreatedAt(now);
        this.setUpdatedAt(now);
    }

    @PreUpdate
    public void preUpdate() {
        LocalDateTime now = LocalDateTime.now();
        this.setUpdatedAt(now);
    }
}
