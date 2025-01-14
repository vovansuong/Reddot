package com.springboot.app.forums.service;

import com.springboot.app.dto.response.ServiceResponse;
import com.springboot.app.forums.dto.ForumDTO;
import com.springboot.app.forums.dto.ForumGroupDTO;
import com.springboot.app.forums.dto.request.LastComment;
import com.springboot.app.forums.dto.search.SearchAll;
import com.springboot.app.forums.entity.Forum;
import com.springboot.app.forums.entity.ForumGroup;
import com.springboot.app.forums.entity.ForumStat;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface ForumService {
    ServiceResponse<ForumGroup> addForumGroup(ForumGroup newForumGroup, String roleName);

    ServiceResponse<Void> deleteForumGroup(ForumGroup forumGroup);

    ServiceResponse<ForumDTO> addForum(Forum newForum, ForumGroup forumGroup, String username);

    ServiceResponse<Void> deleteForum(Forum forum);

    List<ForumGroupDTO> getChildForumsAndForumGroups();

    List<ForumDTO> getAllForum();

    ServiceResponse<ForumDTO> getById(Forum forum);

    ServiceResponse<ForumStat> getForumStat(Long id);

    ServiceResponse<List<SearchAll>> getForumSearch(String keyword);

    ServiceResponse<List<ForumGroup>> voteSortByOrderForumGroup(Long id, String type);

    ServiceResponse<LastComment> getLastCommentServiceResponse(Long id);

}
