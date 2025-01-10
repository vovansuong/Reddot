package com.springboot.app.forums.service.impl;

import com.springboot.app.dto.response.ServiceResponse;
import com.springboot.app.forums.dto.response.ForumGroupStat;
import com.springboot.app.forums.entity.*;
import com.springboot.app.forums.repository.CommentInfoRepository;
import com.springboot.app.forums.repository.CommentRepository;
import com.springboot.app.forums.repository.DiscussionStatRepository;
import com.springboot.app.forums.repository.ForumStatRepository;
import com.springboot.app.forums.service.ForumStatService;
import com.springboot.app.repository.DiscussionDAO;
import com.springboot.app.repository.StatDAO;
import com.springboot.app.tags.TagRepository;
import net.htmlparser.jericho.Source;
import net.htmlparser.jericho.TextExtractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ForumStatServiceImpl implements ForumStatService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private CommentInfoRepository commentInfoRepository;

    @Autowired
    private ForumStatRepository forumStatRepository;

    @Autowired
    private DiscussionStatRepository discussionStatRepository;

    @Autowired
    private StatDAO statDAO;

    @Autowired
    private DiscussionDAO discussionDAO;

    @Autowired
    private TagRepository tagRepository;

    @Override
    public ServiceResponse<ForumStat> syncForumStat(Forum forum) {
        ServiceResponse<ForumStat> response = new ServiceResponse<>();
        response.setDataObject(refreshForumStatFromDB(forum));
        return response;
    }


    private ForumStat refreshForumStatFromDB(Forum forum) {
        ForumStat forumStat = forum.getStat();
        forumStat.setDiscussionCount(forum.getDiscussions().size());
        forumStat.setCommentCount(commentRepository.countComment(forum).longValue());

        //last comment
        Comment lastComment = commentRepository.findLatestComment(forum);
        CommentInfo lastCommentInfo = forumStat.getLastComment();

        if (lastComment != null) {
            // has comment
            if (lastCommentInfo == null) {
                lastCommentInfo = copyToCommentInfo(lastComment);
                commentInfoRepository.save(lastCommentInfo);
                forumStat.setLastComment(lastCommentInfo);
            }
        } else {
            // no comment
            if (lastCommentInfo != null) {
                // remove last comment
                forumStat.setLastComment(null);
                commentInfoRepository.delete(lastCommentInfo);
            }
        }

        forumStatRepository.save(forumStat);
        return forumStat;
    }

    private CommentInfo copyToCommentInfo(Comment comment) {
        CommentInfo commentInfo = new CommentInfo();
        commentInfo.setCommentId(comment.getId());
        commentInfo.setCommenter(comment.getCreatedBy());
        commentInfo.setCommentDate(comment.getCreatedAt());
        commentInfo.setTitle(comment.getTitle());

        String contentAbbr = new TextExtractor(new Source(comment.getContent())).toString();
        commentInfo.setContentAbbr(contentAbbr.length() > 100 ?
                contentAbbr.substring(0, 97) + "..." : contentAbbr);
        return commentInfo;
    }

    @Override
    public ServiceResponse<DiscussionStat> syncDiscussionStat(Discussion discussion) {
        ServiceResponse<DiscussionStat> response = new ServiceResponse<>();
        response.setDataObject(refreshDiscussionStatFromDB(discussion));
        return response;
    }

    private DiscussionStat refreshDiscussionStatFromDB(Discussion discussion) {
        DiscussionStat discussionStat = discussion.getStat();

        discussionStat.setCommentCount(statDAO.countCommentByDiscussion(discussion).longValue());
        // refresh commenter map
        discussion.getStat().setCommentors(statDAO.getCommentorMap(discussion));

        Comment lastComment = statDAO.findLatestCommentByDiscussion(discussion);
        CommentInfo commentInfo = copyToCommentInfo(lastComment);
        commentInfoRepository.save(commentInfo);

        discussionStat.setLastComment(commentInfo);
        discussionStatRepository.save(discussionStat);
        return discussionStat;
    }


    @Override
    public ServiceResponse<ForumGroupStat> getForumGroupStat() {
        ServiceResponse<ForumGroupStat> response = new ServiceResponse<>();
        ForumGroupStat forumGroupStat = discussionDAO.getForumGroupStat();
        forumGroupStat.setTotalTags(tagRepository.countByDisabled(true));
        response.setDataObject(forumGroupStat);
        return response;
    }
}
