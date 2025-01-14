package com.springboot.app.forums.entity;

import com.springboot.app.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "file_info")
@Data
public class FileInfo extends BaseEntity {
    @Column(name = "name", length = 200)
    private String name;

    @Column(name = "content_type", length = 200)
    private String contentType;

    @Column(name = "url", length = 200)
    private String path;

}
