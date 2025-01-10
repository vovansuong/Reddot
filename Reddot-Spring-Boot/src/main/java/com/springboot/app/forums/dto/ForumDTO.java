package com.springboot.app.forums.dto;

import com.springboot.app.forums.entity.ForumStat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ForumDTO {
    private Long id;
    private String title;
    private String description;
    private String icon;
    private String color;
    private boolean active;
    private Integer sortOrder;
    private String createdBy;
    private String updatedBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long idForumGroup;
    private ForumGroupDTO forumGroup;
    private ForumStat stat;
}