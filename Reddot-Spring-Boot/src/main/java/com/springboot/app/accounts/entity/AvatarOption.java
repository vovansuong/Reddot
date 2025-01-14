package com.springboot.app.accounts.entity;

import com.springboot.app.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
@Table(name = "avatar_option")
public class AvatarOption extends BaseEntity {
    @Column(name = "max_file_size")
    private int maxFileSize;
    @Column(name = "max_width")
    private int maxWidth;
    @Column(name = "max_height")
    private int maxHeight;
}
