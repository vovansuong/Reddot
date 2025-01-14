package com.springboot.app.bannedKeyword;

import com.springboot.app.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "banned_keywords")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BannedKeyword extends BaseEntity {
    @Column(name = "keyword", length = 100)
    private String keyword;
}
