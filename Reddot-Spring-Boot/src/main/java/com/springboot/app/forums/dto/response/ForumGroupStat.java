package com.springboot.app.forums.dto.response;

import lombok.Data;

@Data
public class ForumGroupStat {
    private Long totalForums;
    private Long totalDiscussions;
    private Long totalComments;
    private Long totalTags;
}
