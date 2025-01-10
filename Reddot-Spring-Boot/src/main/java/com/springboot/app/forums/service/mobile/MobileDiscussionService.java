package com.springboot.app.forums.service.mobile;

import com.springboot.app.dto.response.ServiceResponse;
import com.springboot.app.forums.dto.request.MobileAllDiscussion;

import java.util.List;

public interface MobileDiscussionService {
    ServiceResponse<List<MobileAllDiscussion>> getAllDiscussion(String title);
}
