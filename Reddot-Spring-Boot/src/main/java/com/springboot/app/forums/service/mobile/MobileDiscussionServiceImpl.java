package com.springboot.app.forums.service.mobile;

import com.springboot.app.accounts.entity.User;
import com.springboot.app.accounts.repository.UserRepository;
import com.springboot.app.bagdes.Badge;
import com.springboot.app.dto.response.ServiceResponse;
import com.springboot.app.forums.dto.request.MobileAllDiscussion;
import com.springboot.app.forums.dto.response.Author;
import com.springboot.app.forums.entity.Discussion;
import com.springboot.app.forums.repository.DiscussionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MobileDiscussionServiceImpl implements MobileDiscussionService {

    @Autowired
    private DiscussionRepository discussionRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional(readOnly = false)
    public ServiceResponse<List<MobileAllDiscussion>> getAllDiscussion(String title) {
        ServiceResponse<List<MobileAllDiscussion>> response = new ServiceResponse<>();
        List<Discussion> discussions = discussionRepository.searchDiscussionByTitle(title);
        List<MobileAllDiscussion> mobileAllDiscussions = discussions.stream().map(discussion -> {
            MobileAllDiscussion mobileAllDiscussion = new MobileAllDiscussion();
            mobileAllDiscussion.setId(discussion.getId());
            mobileAllDiscussion.setTitle(discussion.getTitle());
            mobileAllDiscussion.setCreatedDate(discussion.getCreatedAt());

            User user = userRepository.findByUsername(discussion.getCreatedBy()).orElse(null);
            Author author = new Author();

            if (user != null) {
                Badge badge = user.getStat().getBadge();
                if (badge != null) {
                    author.setBadgeName(badge.getName());
                    author.setBadgeIcon(badge.getIcon());
                    author.setBadgeColor(badge.getColor());
                }
                author.setUsername(user.getUsername());
                author.setAvatar(user.getAvatar());
                author.setImageUrl(user.getImageUrl());

                mobileAllDiscussion.setAuthor(author);
            }
            return mobileAllDiscussion;
        }).collect(Collectors.toList());

        response.setDataObject(mobileAllDiscussions);
        return response;
    }
}
