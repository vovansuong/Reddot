package com.springboot.app.bagdes;

import com.springboot.app.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode(callSuper = true)
@Entity(name = "badges")
@Setter
@Getter
public class Badge extends BaseEntity {
    @Column(name = "name", length = 50)
    private String name;
    @Column(name = "description")
    private String description;
    @Column(name = "icon", length = 50)
    private String icon;
    @Column(name = "color", length = 50)
    private String color;
    @Column(name = "action")
    private boolean action;
    @Column(name = "total_score")
    private long totalScore;
    @Column(name = "total_discussion")
    private long totalDiscussion;
    @Column(name = "total_comment")
    private long totalComment;
}
