package com.springboot.app.forums.dto;

import com.springboot.app.forums.entity.Forum;
import com.springboot.app.forums.entity.ForumGroup;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ForumAndForumGroupDTO {
    private ForumGroup forumGroup;
    private Forum newForum;
}