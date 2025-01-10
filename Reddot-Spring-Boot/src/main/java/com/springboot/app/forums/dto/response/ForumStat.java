package com.springboot.app.forums.dto.response;

import lombok.Data;

@Data
public class ForumStat {
    private Long id;
    private String title;
    private Long discussionCount;
    private Long commentCount;
}
