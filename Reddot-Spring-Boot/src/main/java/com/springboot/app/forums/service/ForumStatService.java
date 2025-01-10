package com.springboot.app.forums.service;

import com.springboot.app.dto.response.ServiceResponse;
import com.springboot.app.forums.dto.response.ForumGroupStat;
import com.springboot.app.forums.entity.Discussion;
import com.springboot.app.forums.entity.DiscussionStat;
import com.springboot.app.forums.entity.Forum;
import com.springboot.app.forums.entity.ForumStat;

public interface ForumStatService {
    ServiceResponse<ForumStat> syncForumStat(Forum forum);

    ServiceResponse<DiscussionStat> syncDiscussionStat(Discussion discussion);

    ServiceResponse<ForumGroupStat> getForumGroupStat();
}
