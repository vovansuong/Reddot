package com.springboot.app.tags;

import com.springboot.app.dto.response.PaginateResponse;
import com.springboot.app.dto.response.ServiceResponse;

public interface TagService {
    PaginateResponse getAllTags(int pageNo, int pageSize, String orderBy, String sortDir, String keyword);

    ServiceResponse<Tag> createNewTag(Tag newTag);

    ServiceResponse<Tag> updateTag(Tag tagToUpdate);

    ServiceResponse<Void> deleteTag(Tag tagToDelete);

}
