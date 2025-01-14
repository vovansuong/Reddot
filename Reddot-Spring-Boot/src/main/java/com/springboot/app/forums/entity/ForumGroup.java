package com.springboot.app.forums.entity;

import com.springboot.app.model.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ForumGroup extends BaseEntity {
    @Column(name = "title", length = 100, unique = true)
    private String title;
    @Column(name = "icon", length = 50)
    private String icon;
    @Column(name = "color", length = 50)
    private String color;
    @OneToMany(mappedBy = "forumGroup", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @OrderBy("sortOrder ASC")
    private List<Forum> forums; // use List instead of Set to maintain the order of forums
    @Column(name = "manager", length = 50)
    private String manager;
    @Column(name = "sort_order")
    private Integer sortOrder;
}
