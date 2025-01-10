package com.springboot.app.tags;

import com.springboot.app.dto.response.PaginateResponse;
import com.springboot.app.dto.response.ServiceResponse;
import com.springboot.app.forums.service.SystemInfoService;
import com.springboot.app.repository.DiscussionDAO;
import com.springboot.app.repository.GenericDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TagServiceImpl implements TagService {

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private GenericDAO genericDAO;

    @Autowired
    private SystemInfoService systemInfoService;
    @Autowired
    private DiscussionDAO discussionDAO;

    @Override
    @Transactional(readOnly = true)
    public PaginateResponse getAllTags(int pageNo, int pageSize, String orderBy, String sortDir, String keyword) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(orderBy).ascending()
                : Sort.by(orderBy).descending();

        // create Pageable instance
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
        // get the list of users from the UserRepository and return it as a Page object
        Page<Tag> tagsPage = tagRepository.searchByLable(keyword, pageable);

        return new PaginateResponse(tagsPage.getNumber() + 1, tagsPage.getSize(), tagsPage.getTotalPages(),
                tagsPage.getContent().size(), tagsPage.isLast(), tagsPage.getContent());
    }

    @Override
    @Transactional(readOnly = false)
    public ServiceResponse<Tag> createNewTag(Tag newTag) {
        ServiceResponse<Tag> response = new ServiceResponse<>();

        Integer maxSortOrder = tagRepository.findTopSortOrder();
        if (maxSortOrder == null) {
            maxSortOrder = 1;
        } else {
            maxSortOrder++;
        }

        newTag.setSortOrder(maxSortOrder);
        newTag.setDisabled(true);
        tagRepository.save(newTag);
        response.setDataObject(newTag);

        return response;
    }

    @Override
    @Transactional(readOnly = false)
    public ServiceResponse<Tag> updateTag(Tag tagToUpdate) {
        ServiceResponse<Tag> response = new ServiceResponse<>();
        tagRepository.save(tagToUpdate);
        response.setDataObject(tagToUpdate);
        return response;
    }

    @Override
    @Transactional(readOnly = false)
    public ServiceResponse<Void> deleteTag(Tag tagToDelete) {
        ServiceResponse<Void> response = new ServiceResponse<>();

        tagRepository.delete(tagToDelete);

        SystemInfoService.Statistics systemStat = systemInfoService.getStatistics().getDataObject();
        systemStat.addTagCount(-1);

        return response;
    }

}
