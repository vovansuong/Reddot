package com.springboot.app.forums.service.impl;

import com.springboot.app.accounts.entity.User;
import com.springboot.app.accounts.entity.UserStat;
import com.springboot.app.accounts.repository.UserRepository;
import com.springboot.app.accounts.repository.UserStatRepository;
import com.springboot.app.accounts.service.UserStatService;
import com.springboot.app.dto.response.PaginateResponse;
import com.springboot.app.dto.response.ServiceResponse;
import com.springboot.app.forums.dto.DiscussionDTO;
import com.springboot.app.forums.dto.request.DiscussionCheckRole;
import com.springboot.app.forums.dto.request.LastComment;
import com.springboot.app.forums.dto.response.Author;
import com.springboot.app.forums.entity.*;
import com.springboot.app.forums.repository.*;
import com.springboot.app.forums.service.CommentService;
import com.springboot.app.forums.service.DiscussionService;
import com.springboot.app.tags.Tag;
import com.springboot.app.tags.TagRepository;
import lombok.extern.slf4j.Slf4j;
import net.htmlparser.jericho.Source;
import net.htmlparser.jericho.TextExtractor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service("discussionService")
@Slf4j
public class DiscussionServiceImpl implements DiscussionService {

    @Autowired
    private DiscussionRepository discussionRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private ForumRepository forumRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private CommentVoteRepository commentVoteRepository;

    @Autowired
    private UserStatService userStatService;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommentService commentService;
    @Autowired
    private DiscussionStatRepository discussionStatRepository;
    @Autowired
    private CommentInfoRepository commentInfoRepository;

    @Autowired
    private UserStatRepository userStatRepository;

    @Override
    @Transactional(readOnly = false)
    public ServiceResponse<Discussion> addDiscussion(Discussion newDiscussion, Comment comment, String username) {
        ServiceResponse<Discussion> response = new ServiceResponse<>();

        // Set basic properties
        comment.setTitle(newDiscussion.getTitle());
        comment.setCreatedBy(username);
        newDiscussion.setCreatedBy(username);

        // Create and associate CommentVote
        CommentVote commentVote = new CommentVote();
        commentVote.setCreatedBy(username);
        commentVote.setCreatedAt(LocalDateTime.now());
        commentVote = commentVoteRepository.save(commentVote);
        comment.setCommentVote(commentVote);

        // Associate comment with discussion and save
        newDiscussion.setComments(new ArrayList<>(List.of(comment)));
        comment.setDiscussion(newDiscussion);

        // Save the discussion (this should cascade and save the comment as well)
        discussionRepository.save(newDiscussion);

        // Ensure comment is persisted before populating statistics
        commentRepository.save(comment);

        if (newDiscussion.getStat() == null) {
            newDiscussion.setStat(new DiscussionStat());
        }

        // Populate discussion statistics
        populateDiscussionStat(comment, newDiscussion, username);

        // Update and save forum statistics
        Forum forum = newDiscussion.getForum();
        ForumStat forumStat = populateForumStat(forum, username, newDiscussion);
        forum.setStat(forumStat);
        forum.getDiscussions().add(newDiscussion);
        forumRepository.save(forum);

        // Update user statistics
        userStatService.syncUserStat(username);

        response.setDataObject(newDiscussion);

        return response;
    }

    private ForumStat populateForumStat(Forum forum, String username, Discussion discussion) {
        ForumStat forumStat = forum.getStat();
        forumStat.setCreatedBy(username);
        forumStat.setCreatedAt(LocalDateTime.now());
        forumStat.setDiscussionCount(forumStat.getDiscussionCount() + 1);
        forumStat.setCommentCount(forumStat.getCommentCount() + 1);
        forumStat.setLastComment(discussion.getStat().getLastComment());
        return forumStat;
    }

    private DiscussionStat populateDiscussionStat(Comment comment, Discussion discussion, String username) {
        // populate discussion stat
        CommentInfo lastComment = new CommentInfo();
        lastComment.setCreatedBy(username);
        lastComment.setCommenter(username);
        lastComment.setCommentDate(comment.getCreatedAt());
        lastComment.setTitle(comment.getTitle());

        String contentAbbreviation = new TextExtractor(new Source(comment.getContent())).toString();
        String contentAbbr = contentAbbreviation.length() > 100 ? contentAbbreviation.substring(0, 97) + "..."
                : contentAbbreviation;

        lastComment.setContentAbbr(contentAbbr);
        lastComment.setCommentId(comment.getId());

        DiscussionStat discussionStat = discussion.getStat();
        discussionStat.setCreatedBy(username);
        discussionStat.setCreatedAt(LocalDateTime.now());
        discussionStat.setCommentors(new java.util.HashMap<>());
        discussionStat.setCommentCount(discussionStat.getCommentCount() + 1);
        discussionStat.setLastComment(lastComment);
        discussionStat.getCommentors().put(username, 1);

        // note: no need to merge in case of update
        return discussionStat;
    }

    @Override
    @Transactional(readOnly = true)
    public ServiceResponse<DiscussionDTO> getById(Long id) {
        ServiceResponse<DiscussionDTO> response = new ServiceResponse<>();
        Discussion discussion = discussionRepository.findById(id).orElse(null);
        DiscussionDTO dto = modelMapper.map(discussion, DiscussionDTO.class);
        response.setDataObject(dto);
        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public ServiceResponse<List<DiscussionDTO>> getDiscussionsByForum(Long id) {
        ServiceResponse<List<DiscussionDTO>> response = new ServiceResponse<>();
        List<Discussion> discussions = discussionRepository.findDiscussionByForumId(id);
        log.info("Discussions found: {}", discussions.size());
        List<DiscussionDTO> dtos = new ArrayList<>();
        for (Discussion d : discussions) {
            dtos.add(modelMapper.map(d, DiscussionDTO.class));
        }
        response.setDataObject(dtos);
        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public ServiceResponse<List<DiscussionDTO>> getAllDiscussions() {
        ServiceResponse<List<DiscussionDTO>> response = new ServiceResponse<>();
        List<Discussion> discussions = discussionRepository.findAll();
        List<DiscussionDTO> dtos = new ArrayList<>();
        for (Discussion d : discussions) {
            dtos.add(modelMapper.map(d, DiscussionDTO.class));
        }
        response.setDataObject(dtos);
        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public ServiceResponse<Discussion> getDiscussionsById(Long id) {
        ServiceResponse<Discussion> response = new ServiceResponse<>();
        Discussion discussion = discussionRepository.findById(id).orElse(null);
        response.setDataObject(discussion);
        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public PaginateResponse getAllDiscussion(int pageNo, int pageSize, String orderBy, String sortDir, String keyword,
                                             Long forumId) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(orderBy).ascending()
                : Sort.by(orderBy).descending();

        // create Pageable instance
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
        // get the list of users from the UserRepository and return it as a Page object
        Page<Discussion> discussionPage = discussionRepository.searchByTitle(keyword, forumId, pageable);

        // map the list of discussions to list of DiscussionDTO
        Page<DiscussionDTO> discussionDTOPage = discussionPage
                .map(discussion -> modelMapper.map(discussion, DiscussionDTO.class));

        return new PaginateResponse(discussionDTOPage.getNumber() + 1, discussionDTOPage.getSize(),
                discussionDTOPage.getTotalPages(), discussionDTOPage.getContent().size(), discussionDTOPage.isLast(),
                discussionDTOPage.getContent());
    }


    @Override
    @Transactional(readOnly = false)
    public ServiceResponse<DiscussionStat> updateDiscussionViews(Long id) {
        ServiceResponse<DiscussionStat> response = new ServiceResponse<>();
        Discussion discussion = discussionRepository.findById(id).orElse(null);
        if (discussion == null) {
            response.addMessage("Discussion not found");
            return response;
        }
        DiscussionStat discussionStat = discussion.getStat();
        discussionStat.addViewCount(1L);
        discussionStat.setLastViewed(LocalDateTime.now());
        discussion.setStat(discussionStat);
        discussionRepository.save(discussion);
        response.setDataObject(discussionStat);
        return response;
    }

    @Override
    @Transactional(readOnly = false)
    public ServiceResponse<Discussion> addTagsToDiscussion(Long discussionId, List<Long> tagIds) {
        ServiceResponse<Discussion> response = new ServiceResponse<>();

        // Retrieve the discussion from the repository
        Optional<Discussion> discussionOpt = discussionRepository.findById(discussionId);
        if (!discussionOpt.isPresent()) {
            throw new RuntimeException("Discussion not found");
        }

        Discussion discussion = discussionOpt.get();

        // Retrieve all tags associated with the provided tagIds
        List<Tag> tagsToAdd = tagRepository.findAllById(tagIds);

        // Remove tags that are already associated with the discussion
        discussion.getTags().clear();

        // Add new tags to the discussion
        discussion.getTags().addAll(tagsToAdd);

        // Save the updated discussion entity
        discussionRepository.save(discussion);

        response.setDataObject(discussion);
        return response;
    }

    @Override
    @Transactional(readOnly = false)
    public ServiceResponse<List<DiscussionDTO>> getDiscussionsByTagId(Long tagId) {
        ServiceResponse<List<DiscussionDTO>> response = new ServiceResponse<>();
        List<Discussion> discussions = discussionRepository.findDiscussionsByTagId(tagId);
        List<DiscussionDTO> dtos = new ArrayList<>();
        for (Discussion d : discussions) {
            dtos.add(modelMapper.map(d, DiscussionDTO.class));
        }
        response.setDataObject(dtos);
        return response;
    }

    @Override
    public ServiceResponse<LastComment> getLatCommentServiceResponse(Long id) {
        ServiceResponse<LastComment> response = new ServiceResponse<>();
        Discussion discussion = discussionRepository.findById(id).orElse(null);

        if (discussion == null) {
            return response;
        }
        CommentInfo lastCommentInfo = discussion.getStat().getLastComment();

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

    @Override
    @Transactional(readOnly = false)
    public ServiceResponse<Void> deleteDiscussion(Long discussionId) {
        ServiceResponse<Void> response = new ServiceResponse<>();
        Discussion discussion = discussionRepository.findById(discussionId).orElse(null);
        if (discussion == null) {
            response.addMessage("Discussion not found");
            return response;
        }

        List<Tag> tag = discussion.getTags();
        if (tag != null && !tag.isEmpty()) {
            discussion.getTags().clear();
            discussionRepository.save(discussion);
        }

        //find all comments associated with the discussion
        List<Comment> comments = commentRepository.findCommentsByDiscussionId(discussionId);

        //delete all comments
        for (Comment comment : comments) {
            commentService.deleteComment(comment.getId(), discussionId);
        }

        // Delete the discussion
        discussionRepository.delete(discussion);

        //remove the discussion stat
        Map<String, Integer> commentors = discussion.getStat().getCommentors();
        commentors.clear();

        DiscussionStat discussionStat = discussion.getStat();
        discussionStatRepository.delete(discussionStat);

        //update forum stat
        Forum forum = discussion.getForum();
        ForumStat forumStat = forum.getStat();
        forumStat.setDiscussionCount(forumStat.getDiscussionCount() - 1);
        List<Comment> commentsForForum = commentRepository.findAllByForumIdOrderByCreatedAtDesc(forum.getId());
        if (commentsForForum != null && !commentsForForum.isEmpty()) {
            CommentInfo forumCommentInfo = commentInfoRepository.findByCommentId(commentsForForum.getFirst().getId());
            forumStat.setLastComment(forumCommentInfo);
        } else {
            forumStat.setLastComment(null);
        }
        forumRepository.save(forum);

        //update user stat
        User user = userRepository.findByUsername(discussion.getCreatedBy()).orElse(null);
        if (user == null) {
            return null;
        }
        UserStat userStat = user.getStat();
        userStat.setDiscussionCount(userStat.getDiscussionCount() - 1);

        CommentInfo userLastComment = userStat.getLastComment();
        if (userLastComment != null) {
            Comment commentForUser = commentRepository.findAllByCreatedByOrderByCreatedAtDesc(discussion.getCreatedBy()).getFirst();
            if (commentForUser != null) {
                userLastComment.setCommentId(commentForUser.getId());
                userLastComment.setCommenter(commentForUser.getCreatedBy());
                userLastComment.setTitle(commentForUser.getTitle());
                userLastComment.setCommentDate(commentForUser.getCreatedAt());

                String contentAbbreviation = new TextExtractor(new Source(commentForUser.getContent())).toString();
                String contentAbbr = contentAbbreviation.length() > 100 ? contentAbbreviation.substring(0, 97) + "..." : contentAbbreviation;
                userLastComment.setContentAbbr(contentAbbr);
                user.getStat().setLastComment(userLastComment);
            }
            user.getStat().setLastComment(null);
        }
        userRepository.save(user);

        //delete
        CommentInfo lastComment = discussionStat.getLastComment();
        commentInfoRepository.delete(lastComment);

        return response;
    }

    @Override
    @Transactional(readOnly = false)
    public ServiceResponse<DiscussionCheckRole> checkRole(Long discussionId) {
        ServiceResponse<DiscussionCheckRole> response = new ServiceResponse<>();
        Discussion discussion = discussionRepository.findById(discussionId).orElse(null);
        if (discussion == null) {
            response.addMessage("Discussion not found");
            return response;
        }

        Forum forum = discussion.getForum();
        if (forum == null) {
            response.addMessage("Forum not found");
            return response;
        }
        ForumGroup forumGroup = forum.getForumGroup();
        if (forumGroup == null) {
            response.addMessage("Forum group not found");
            return response;
        }

        DiscussionCheckRole discussionCheckRole = new DiscussionCheckRole();
        discussionCheckRole.setDiscussionId(discussionId);
        discussionCheckRole.setRoleName(forumGroup.getManager());
        response.setDataObject(discussionCheckRole);
        return response;
    }

}
