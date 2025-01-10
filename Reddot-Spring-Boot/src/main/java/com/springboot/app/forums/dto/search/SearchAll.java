package com.springboot.app.forums.dto.search;

import com.springboot.app.forums.dto.response.Author;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SearchAll {
    private Long id;
    private String title;
    private LocalDateTime createdAt;
    private Author author;
    private String description;
    private String type;
    private Long discussionId;
}
