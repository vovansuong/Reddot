package com.springboot.app.forums.dto.request;

import com.springboot.app.forums.dto.response.Author;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MobileAllDiscussion {
    private Long id;
    private String title;
    private LocalDateTime createdDate;
    private Author author;
}
