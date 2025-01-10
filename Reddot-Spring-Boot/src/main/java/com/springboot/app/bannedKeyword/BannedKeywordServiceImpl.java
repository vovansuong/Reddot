package com.springboot.app.bannedKeyword;

import com.springboot.app.dto.response.PaginateResponse;
import com.springboot.app.dto.response.ServiceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BannedKeywordServiceImpl implements BannedKeyWordService {

    @Autowired
    private BannedKeywordRepository bannedKeywordRepository;

    //get all banned keywords
    @Override
    public ServiceResponse<List<BannedKeyword>> getAllBannedKeywords() {
        ServiceResponse<List<BannedKeyword>> response = new ServiceResponse<>();
        List<BannedKeyword> bannedKeywords = bannedKeywordRepository.findAll();
        response.setDataObject(bannedKeywords);
        return response;
    }

    @Override
    @Transactional(readOnly = false)
    public PaginateResponse getPageBannedKeyword(int pageNo, int pageSize, String orderBy, String sortDir, String keyword) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(orderBy).ascending()
                : Sort.by(orderBy).descending();

        // create Pageable instance
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
        // get the list of users from the UserRepository and return it as a Page object
        Page<BannedKeyword> bannedKeywordsPage = bannedKeywordRepository.searchByKeyword(keyword, pageable);

        return new PaginateResponse(bannedKeywordsPage.getNumber() + 1, bannedKeywordsPage.getSize(), bannedKeywordsPage.getTotalPages(),
                bannedKeywordsPage.getContent().size(), bannedKeywordsPage.isLast(), bannedKeywordsPage.getContent());
    }


    //save a banned keyword
    @Override
    public ServiceResponse<BannedKeyword> saveBannedKeyword(BannedKeyword bannedKeyword) {
        ServiceResponse<BannedKeyword> response = new ServiceResponse<>();
        BannedKeyword savedBannedKeyword = bannedKeywordRepository.save(bannedKeyword);
        response.setDataObject(savedBannedKeyword);
        return response;
    }

    //delete a banned keyword
    @Override
    public ServiceResponse<BannedKeyword> deleteBannedKeyword(Long id) {
        ServiceResponse<BannedKeyword> response = new ServiceResponse<>();
        bannedKeywordRepository.deleteById(id);
        return response;
    }
}
