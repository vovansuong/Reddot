package com.springboot.app.forums.entity;

import com.springboot.app.model.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Forum extends BaseEntity {
    @Column(name = "title", length = 100, unique = true)
    private String title;
    @Column(name = "description", length = 200)
    private String description;
    @Column(name = "icon", length = 30)
    private String icon;
    @Column(name = "color", length = 30)
    private String color;
    @Column(name = "active")
    private boolean active;
    @Column(name = "sort_order")
    private Integer sortOrder;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "forum_group_id", foreignKey = @ForeignKey(name = "FK_FORUM_FORUM_GROUP"))
    private ForumGroup forumGroup; // point to group that this forum belongs to
    @OneToMany(mappedBy = "forum", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @OrderBy("createdAt DESC")
    private List<Discussion> discussions = new ArrayList<>();
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "forum_stat_id", foreignKey = @ForeignKey(name = "FK_FORUM_FORUM_STAT"))
    private ForumStat stat;
}
