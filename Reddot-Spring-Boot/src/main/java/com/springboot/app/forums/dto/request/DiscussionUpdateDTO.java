package com.springboot.app.forums.dto.request;

import lombok.Data;

@Data
public class DiscussionUpdateDTO {
    private Long id;
    private boolean closed;
    private boolean important;
}
