package com.springboot.app.accounts.service.impl;

import com.springboot.app.accounts.dto.responce.UserStatResponse;
import com.springboot.app.accounts.entity.User;
import com.springboot.app.accounts.entity.UserStat;
import com.springboot.app.accounts.repository.UserRepository;
import com.springboot.app.accounts.repository.UserStatRepository;
import com.springboot.app.accounts.service.UserStatService;
import com.springboot.app.dto.response.PaginateResponse;
import com.springboot.app.dto.response.ServiceResponse;
import com.springboot.app.forums.entity.Comment;
import com.springboot.app.forums.entity.CommentInfo;
import com.springboot.app.forums.repository.CommentInfoRepository;
import com.springboot.app.repository.CommentDAO;
import com.springboot.app.repository.StatDAO;
import com.springboot.app.repository.VoteDAO;
import com.springboot.app.security.jwt.JwtUtils;
import net.htmlparser.jericho.Source;
import net.htmlparser.jericho.TextExtractor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserStatServiceImpl implements UserStatService {

    private static final Logger logger = LoggerFactory.getLogger(UserStatServiceImpl.class);

    @Autowired
    private UserStatRepository userStatRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StatDAO statDAO;
    @Autowired
    private VoteDAO voteDAO;
    @Autowired
    private CommentDAO commentDAO;
    @Autowired
    private CommentInfoRepository commentInfoRepository;

    @Override
    public PaginateResponse getAllUserStats(int pageNo, int pageSize, String orderBy, String sortDir, String search) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(orderBy).ascending()
                : Sort.by(orderBy).descending();
        // create Pageable instance
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
        // get the list of users from the UserRepository and return it as a Page object
        Page<UserStat> usersPage = userStatRepository.searchByUsername(search, pageable);

        return new PaginateResponse(
                usersPage.getNumber() + 1,
                usersPage.getSize(),
                usersPage.getTotalPages(),
                usersPage.getContent().size(),
                usersPage.isLast(),
                usersPage.getContent());
    }

    @Override
    public PaginateResponse getAllUserStatsWithIgnoreAdmin(int pageNo, int pageSize, String orderBy, String sortDir, String search) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(orderBy).ascending()
                : Sort.by(orderBy).descending();
        // create Pageable instance
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
        // get the list of users from the UserRepository and return it as a Page object
        Page<UserStatResponse> usersPage = userRepository.searchByUsernameOrNameWithIgnoreAdmin(search, pageable)
                .map(User::toUserStatResponse);

        return new PaginateResponse(
                usersPage.getNumber() + 1,
                usersPage.getSize(),
                usersPage.getTotalPages(),
                usersPage.getContent().size(),
                usersPage.isLast(),
                usersPage.getContent());
    }


    public ServiceResponse<UserStat> updateProfileViewed(String username) {
        ServiceResponse<UserStat> response = new ServiceResponse<>();
        var session = JwtUtils.getSession();
        if (session.getUsername().equals(username)) {
            response.addMessage("You can't view your own profile view count");
            return response;
        }

        User user = userRepository.findByUsername(username).orElse(null);
        if (user == null) {
            response.addMessage("User not found");
            return response;
        }
        UserStat userStat = user.getStat();
        userStat.setProfileViewed(userStat.getProfileViewed() + 1);
        userStatRepository.save(userStat);

        response.setDataObject(userStat);
        response.addMessage("Profile viewed updated successfully");
        return response;
    }

    /**
     * User statistics
     */

    public ServiceResponse<UserStat> syncUserStat(String username) {
        ServiceResponse<UserStat> response = new ServiceResponse<>();
        UserStat userStat = refreshUserStat(username);
        response.setDataObject(userStat);
        return response;
    }

    private UserStat refreshUserStat(String username) {
        User user = userRepository.findByUsername(username).orElse(null);
        if (user == null) {
            return null;
        }
        UserStat userStat = user.getStat();

        //set user statistics
        userStat.setCommentCount(statDAO.countComment(username).longValue());
        userStat.setDiscussionCount(statDAO.countDiscussion(username).longValue());
        userStat.setReputation(voteDAO.getReputation4User(username).longValue());

        //comment info
        Comment lastComment = null;
        List<Comment> comments = commentDAO.getLatestCommentsForUser(username, 1);

        if (!comments.isEmpty()) {
            lastComment = comments.getFirst();
        }
        CommentInfo latestCommentInfo = userStat.getLastComment();

        if (lastComment != null) {
            if (latestCommentInfo == null) {
                latestCommentInfo = new CommentInfo();
                latestCommentInfo.setCreatedBy(username);
                copyToCommentInfo(lastComment, latestCommentInfo);
                commentInfoRepository.save(latestCommentInfo);
                userStat.setLastComment(latestCommentInfo);
            }
            copyToCommentInfo(lastComment, latestCommentInfo);
        } else {
            // remove last comment info if no comment found for user in the system
            if (latestCommentInfo != null) {
                userStat.setLastComment(null);
                commentInfoRepository.delete(latestCommentInfo);
            }
        }

        userStatRepository.save(userStat);
        return userStat;
    }

    private void copyToCommentInfo(Comment comment, CommentInfo commentInfo) {
        commentInfo.setCommenter(comment.getCreatedBy());
        commentInfo.setCommentId(comment.getId());
        commentInfo.setCommentDate(comment.getCreatedAt());
        commentInfo.setTitle(comment.getTitle());

        String contentAbbr = new TextExtractor(new Source(comment.getContent())).toString();
        commentInfo.setContentAbbr(contentAbbr.length() > 100 ?
                contentAbbr.substring(0, 97) + "..." : contentAbbr);
    }

}
