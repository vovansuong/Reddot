package com.springboot.app.admin.service;

import com.springboot.app.accounts.repository.UserRepository;
import com.springboot.app.admin.dto.DashBoardResponse;
import com.springboot.app.admin.dto.DataForumGroupResponse;
import com.springboot.app.dto.response.ServiceResponse;
import com.springboot.app.forums.repository.CommentRepository;
import com.springboot.app.forums.repository.DiscussionRepository;
import com.springboot.app.forums.repository.ForumRepository;
import com.springboot.app.repository.DiscussionDAO;
import com.springboot.app.tags.TagRepository;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminDashboardServiceImpl implements AdminDashboardService {
    private static final Logger logger = LoggerFactory.getLogger(AdminDashboardServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ForumRepository forumRepository;

    @Autowired
    private DiscussionRepository discussionRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private TagRepository tagRepository;

    @Resource
    private DiscussionDAO discussionDAO;

    @Override
    public ServiceResponse<DashBoardResponse> getDashboardData() {
        ServiceResponse<DashBoardResponse> response = new ServiceResponse<>();
        DashBoardResponse dashBoardResponse = new DashBoardResponse();
        dashBoardResponse.setTotalUsers(userRepository.count());
        dashBoardResponse.setTotalForums(forumRepository.count());
        dashBoardResponse.setTotalDiscussions(discussionRepository.count());
        dashBoardResponse.setTotalComments(commentRepository.count());
        dashBoardResponse.setTotalTags(tagRepository.count());

        response.setDataObject(dashBoardResponse);
        return response;
    }

    @Override
    public ServiceResponse<List<DataForumGroupResponse>> getDataByForumGroup() {
        ServiceResponse<List<DataForumGroupResponse>> response = new ServiceResponse<>();
        List<DataForumGroupResponse> dataForumGroupResponses = discussionDAO.getForumGroupData2();
        System.out.println("dataForumGroupResponses: " + dataForumGroupResponses.size());
        response.setDataObject(dataForumGroupResponses);
        return response;
    }


}
