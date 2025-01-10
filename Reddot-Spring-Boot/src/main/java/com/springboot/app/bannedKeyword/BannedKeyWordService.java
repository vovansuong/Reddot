package com.springboot.app.bannedKeyword;

import com.springboot.app.dto.response.PaginateResponse;
import com.springboot.app.dto.response.ServiceResponse;

import java.util.List;

public interface BannedKeyWordService {
    ServiceResponse<List<BannedKeyword>> getAllBannedKeywords();

    ServiceResponse<BannedKeyword> saveBannedKeyword(BannedKeyword bannedKeyword);

    ServiceResponse<BannedKeyword> deleteBannedKeyword(Long id);

    PaginateResponse getPageBannedKeyword(int pageNo, int pageSize, String orderBy, String sortDir, String keyword);
}
