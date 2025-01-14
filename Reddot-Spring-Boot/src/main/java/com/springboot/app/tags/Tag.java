package com.springboot.app.tags;

import com.springboot.app.model.BaseEntity;
import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "tags")
@Data
@NoArgsConstructor
@Cacheable
public class Tag extends BaseEntity {
    @Column(name = "lable")
    private String label;
    @Column(name = "icon", length = 30)
    private String icon;
    @Column(name = "color", length = 30)
    private String color;
    @Column(name = "disabled")
    private boolean disabled;
    @Column(name = "sort_order")
    private Integer sortOrder;

    public Tag(String label, String icon, String color) {
        this.label = label;
        this.icon = icon;
        this.color = color;
    }
}
