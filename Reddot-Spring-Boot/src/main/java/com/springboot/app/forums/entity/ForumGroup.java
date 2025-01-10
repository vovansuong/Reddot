package com.springboot.app.forums.entity;

import com.springboot.app.model.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "forum_groups")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ForumGroup extends BaseEntity {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;
    @Column(name = "title", length = 100, unique = true)
    private String title;
    @Column(name = "icon", length = 50)
    private String icon;
    @Column(name = "color", length = 50)
    private String color;
    /**
     * Note: set cascade type to CascadeType.REMOVE to delete all forums in this
     * group when the group is deleted
     */
    @OneToMany(mappedBy = "forumGroup", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @OrderBy("sortOrder ASC")
    private List<Forum> forums; // use List instead of Set to maintain the order of forums
    @Column(name = "manager", length = 50)
    private String manager;
    @Column(name = "sort_order")
    private Integer sortOrder;

    @PrePersist
    public void prePersist() {
        this.setCreatedAt(java.time.LocalDateTime.now());
    }

    @PreUpdate
    public void preUpdate() {
        this.setUpdatedAt(java.time.LocalDateTime.now());
    }
}
