package com.springboot.app.forums.dto.request;

import com.springboot.app.forums.dto.response.Author;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LastComment {
    private Author author;
    private String title;
    private String contentAbbr;
    private String commenter;
    private LocalDateTime commentDate;
}
