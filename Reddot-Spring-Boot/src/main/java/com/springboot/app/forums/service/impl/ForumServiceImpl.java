package com.springboot.app.forums.service.impl;

import com.springboot.app.accounts.entity.User;
import com.springboot.app.accounts.repository.UserRepository;
import com.springboot.app.dto.response.ServiceResponse;
import com.springboot.app.forums.dto.ForumDTO;
import com.springboot.app.forums.dto.ForumGroupDTO;
import com.springboot.app.forums.dto.request.LastComment;
import com.springboot.app.forums.dto.response.Author;
import com.springboot.app.forums.dto.search.SearchAll;
import com.springboot.app.forums.entity.CommentInfo;
import com.springboot.app.forums.entity.Forum;
import com.springboot.app.forums.entity.ForumGroup;
import com.springboot.app.forums.entity.ForumStat;
import com.springboot.app.forums.repository.DiscussionRepository;
import com.springboot.app.forums.repository.ForumGroupRepository;
import com.springboot.app.forums.repository.ForumRepository;
import com.springboot.app.forums.repository.ForumStatRepository;
import com.springboot.app.forums.service.ForumService;
import com.springboot.app.forums.service.SystemInfoService;
import com.springboot.app.repository.GenericDAO;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service("forumService")
@Transactional
public class ForumServiceImpl implements ForumService {
    private static final Logger logger = LoggerFactory.getLogger(ForumServiceImpl.class);

    @Autowired
    private ForumGroupRepository forumGroupRepository;

    @Autowired
    private ForumRepository forumRepository;

    @Autowired
    private DiscussionRepository discussionRepository;

    @Autowired
    private ForumStatRepository forumStatRepository;

    @Autowired
    private SystemInfoService systemInfoService;

    @Autowired
    private GenericDAO genericDAO;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserRepository userRepository;

    private ForumGroupDTO convertToDTO(ForumGroup forumGroup) {
        return modelMapper.map(forumGroup, ForumGroupDTO.class);
    }

    private ForumDTO convertToDTO(Forum forum) {
        return modelMapper.map(forum, ForumDTO.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ForumGroupDTO> getChildForumsAndForumGroups() {
        List<ForumGroup> topLevelForumGroups = forumGroupRepository.findAll();
        return topLevelForumGroups.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = false)
    public ServiceResponse<ForumStat> getForumStat(Long id) {
        ServiceResponse<ForumStat> response = new ServiceResponse<>();
        ForumStat forumStat = forumStatRepository.findById(id).orElse(null);
        response.setDataObject(forumStat);
        return response;
    }

    @Override
    public List<ForumDTO> getAllForum() {
        List<Forum> forumGroups = genericDAO.findAll(Forum.class);
        List<ForumDTO> dto = forumGroups.stream().map(this::convertToDTO).collect(Collectors.toList());
        // set idForumGroup for forum = id of forum group
        for (ForumDTO forumDTO : dto) {
            forumDTO.setIdForumGroup(forumDTO.getForumGroup().getId());
        }
        return dto;
    }

    @Override
    public ServiceResponse<Void> deleteForumGroup(ForumGroup forumGroup) {
        ServiceResponse<Void> response = new ServiceResponse<>();

        int sortOrderBy = forumGroup.getSortOrder();
        forumGroupRepository.decrementSortOrder(sortOrderBy);

        // reset all discussions to the root forum group
        resetDiscussions(forumGroup);

        // delete the forum group and its subgroups recursively
        forumGroupRepository.delete(forumGroup);

        return response;
    }

    public void resetDiscussions(ForumGroup forumGroup) {
        for (Forum forum : forumGroup.getForums()) {
            discussionRepository.moveDiscussion(forum, null);
        }
    }

    @Override
    public ServiceResponse<ForumDTO> addForum(Forum newForum, ForumGroup forumGroup, String username) {
        ServiceResponse<ForumDTO> response = new ServiceResponse<>();

        if (forumGroup == null) {
            logger.error("Forum group is null");
            return response;
        }
        Integer maxSortOrder = forumRepository.findTopBySortOrderForForum(forumGroup.getId());

        if (maxSortOrder == null) {
            maxSortOrder = 1;
        } else {
            maxSortOrder++;
        }

        newForum.setSortOrder(maxSortOrder);
        newForum.setActive(true);

        ForumStat forumStat = new ForumStat();
        forumStat.setCreatedAt(LocalDateTime.now());
        forumStat.setCreatedBy(username);
        newForum.setStat(forumStat);

        newForum.setForumGroup(forumGroup);

        newForum = forumRepository.save(newForum);
        // map newForum to ForumDTO
        ForumDTO newForumDTO = convertToDTO(newForum);

        response.setDataObject(newForumDTO);
        return response;
    }

    @Override
    public ServiceResponse<Void> deleteForum(Forum forum) {
        ServiceResponse<Void> response = new ServiceResponse<>();
        // set discussion's reference to forum to null
        discussionRepository.moveDiscussion(forum, null);

        ForumGroup forumGroup = forum.getForumGroup();
        if (forumGroup != null) {
            forumGroup.getForums().remove(forum);
            forumGroupRepository.save(forumGroup);
        }
        SystemInfoService.Statistics systemStat = systemInfoService.getStatistics().getDataObject();
        systemStat.addForumCount(-1);

        // delete the forum
        forumRepository.delete(forum);

        return response;
    }

    @Override
    public ServiceResponse<ForumGroup> addForumGroup(ForumGroup newForumGroup, String roleName) {

        ServiceResponse<ForumGroup> response = new ServiceResponse<>();

        Integer maxSortOrder = forumRepository.findTopBySortOrder();
        if (maxSortOrder == null) {
            maxSortOrder = 1;
        } else {
            maxSortOrder++;
        }
        newForumGroup.setSortOrder(maxSortOrder);
        newForumGroup.setManager(roleName);

        forumGroupRepository.save(newForumGroup);
        response.setDataObject(newForumGroup);

        // increment forum group count
        SystemInfoService.Statistics systemStat = systemInfoService.getStatistics().getDataObject();
        systemStat.addForumGroupCount(1);

        return response;
    }

    @Override
    public ServiceResponse<ForumDTO> getById(Forum forum) {
        ServiceResponse<ForumDTO> response = new ServiceResponse<>();
        ForumDTO forumDTO = convertToDTO(forum);
        response.setDataObject(forumDTO);
        return response;
    }

    @Override
    public ServiceResponse<List<SearchAll>> getForumSearch(String keyword) {
        ServiceResponse<List<SearchAll>> response = new ServiceResponse<>();
        List<Forum> forums = forumRepository.findByTitle(keyword);
        List<SearchAll> forumSearch = forums.stream().map(forum -> {
            SearchAll search = new SearchAll();
            search.setId(forum.getId());
            search.setTitle(forum.getTitle());
            search.setCreatedAt(forum.getCreatedAt());
            search.setDescription(forum.getDescription());
            search.setType("forum");
            return search;
        }).collect(Collectors.toList());
        response.setDataObject(forumSearch);
        return response;
    }

    @Override
    @Transactional
    public ServiceResponse<List<ForumGroup>> voteSortByOrderForumGroup(Long id, String type) {
        ServiceResponse<List<ForumGroup>> response = new ServiceResponse<>();
        ForumGroup forumGroup = forumGroupRepository.findById(id).orElse(null);
        if (forumGroup == null) {
            return response;
        }

        Integer currentSortOrder = forumGroup.getSortOrder();
        ForumGroup forumGroupSwap;
        int swapSortOrder;

        if ("up".equals(type)) {
            swapSortOrder = currentSortOrder - 1;
            if (swapSortOrder < 1) {
                return response;
            }
            forumGroupSwap = forumGroupRepository.findBySortOrder(swapSortOrder);
        } else {
            swapSortOrder = currentSortOrder + 1;
            if (swapSortOrder > forumRepository.findTopBySortOrder()) {
                return response;
            }
            forumGroupSwap = forumGroupRepository.findBySortOrder(currentSortOrder + 1);
        }

        if (forumGroupSwap != null) {
            int temp = forumGroup.getSortOrder();
            forumGroup.setSortOrder(forumGroupSwap.getSortOrder());
            forumGroupSwap.setSortOrder(temp);
            forumGroupRepository.save(forumGroup);
            forumGroupRepository.save(forumGroupSwap);
        }

        response.setDataObject(forumGroupRepository.findAll());
        return response;
    }

    @Override
    @Transactional
    public ServiceResponse<LastComment> getLastCommentServiceResponse(Long id) {
        ServiceResponse<LastComment> response = new ServiceResponse<>();
        Forum forum = forumRepository.findById(id).orElse(null);
        if (forum == null) {
            return response;
        }

        CommentInfo lastCommentInfo = forum.getStat().getLastComment();

        Author author = new Author();
        User user = userRepository.findByUsername(lastCommentInfo.getCreatedBy()).orElse(null);
        if (user != null) {
            author.setUsername(user.getUsername());
            author.setAvatar(user.getAvatar());
            author.setImageUrl(user.getImageUrl());
        }
        LastComment lastComment = new LastComment();
        lastComment.setAuthor(author);
        lastComment.setCommentDate(lastCommentInfo.getCommentDate());
        lastComment.setContentAbbr(lastCommentInfo.getContentAbbr());
        lastComment.setCommenter(lastCommentInfo.getCreatedBy());
        lastComment.setTitle(lastCommentInfo.getTitle());

        response.setDataObject(lastComment);
        return response;
    }

}
