package com.springboot.app.forums.dto;

import com.springboot.app.forums.entity.DiscussionStat;
import com.springboot.app.tags.TagDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DiscussionDTO {
    private Long id;
    private String title;
    private boolean closed;
    private boolean sticky;
    private boolean important;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;
    private List<CommentDTO> comments;
    private ForumDTO forum;
    private DiscussionStat stat;
    private List<TagDTO> tags;
}
