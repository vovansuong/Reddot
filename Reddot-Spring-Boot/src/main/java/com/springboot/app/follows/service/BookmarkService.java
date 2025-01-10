package com.springboot.app.follows.service;

import com.springboot.app.dto.response.ServiceResponse;
import com.springboot.app.follows.dto.request.BookmarkRequest;

public interface BookmarkService {
    ServiceResponse<Void> registerBookmark(BookmarkRequest bookmarkRequest);
}
