package com.springboot.app.forums.service.mobile;

import com.springboot.app.dto.response.ServiceResponse;
import com.springboot.app.forums.dto.request.MobileCommentRequest;
import com.springboot.app.forums.dto.request.MobileDiscussionRequest;
import com.springboot.app.forums.dto.response.MobileDiscussionResponse;
import com.springboot.app.forums.dto.response.MobileForumResponse;
import com.springboot.app.forums.dto.response.MobileGroupResponse;
import com.springboot.app.forums.dto.response.ViewCommentResponse;

import java.util.List;

public interface MobileForumsService {

    ServiceResponse<List<MobileGroupResponse>> getAllForumGroups();

    ServiceResponse<List<MobileForumResponse>> getAllForumByAllGroup();

    ServiceResponse<List<MobileForumResponse>> getAllForumsByGroupId(Long groupId);

    ServiceResponse<List<ViewCommentResponse>> getAllCommentByDiscussionId(Long id);

    ServiceResponse<byte[]> getContentByCommentId(Long id);

    ServiceResponse<MobileDiscussionResponse> addNewDiscussion(MobileDiscussionRequest newDiscussion);

    ServiceResponse<ViewCommentResponse> addNewComment(MobileCommentRequest newComment);
}
