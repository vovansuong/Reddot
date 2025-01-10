package com.springboot.app.forums.service;

import com.springboot.app.accounts.repository.UserRepository;
import com.springboot.app.dto.response.ServiceResponse;
import com.springboot.app.forums.entity.CommentInfo;
import com.springboot.app.forums.repository.CommentRepository;
import com.springboot.app.forums.repository.DiscussionRepository;
import com.springboot.app.forums.repository.ForumRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

@Service("systemInfoService")
public class SystemInfoService {
    private static final Logger logger = Logger.getLogger(SystemInfoService.class.getName());
    /**
     * List of logged on users
     */
    private final Set<String> loggedOnUsers = Collections.synchronizedSet(new HashSet<String>());
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private DiscussionRepository discussionRepository;
    @Autowired
    private ForumRepository forumRepository;
    @Autowired
    private UserRepository userRepository;
    private Statistics statistics = new Statistics();
    /**
     * Current user sessions (both logged-on and anonymous sessions)
     */
    private AtomicInteger sessionCount = new AtomicInteger(0);

    @Transactional(readOnly = true)
    public ServiceResponse<Void> refreshStatistics() {

        logger.info("Refreshing statistics data");

        ServiceResponse<Void> response = new ServiceResponse<>();

        this.statistics = statisticsFromDB();

        return response;
    }

    private Statistics statisticsFromDB() {

        Statistics stat = new Statistics();
        stat.setUserCount(userRepository.count());
        stat.setCommentCount(commentRepository.count());
        stat.setDiscussionCount(discussionRepository.count());
        stat.setForumCount(forumRepository.count());


        return stat;
    }

    @Transactional(readOnly = true)
    public ServiceResponse<Statistics> getStatistics() {

        ServiceResponse<Statistics> response = new ServiceResponse<>();

        response.setDataObject(statistics);

        return response;
    }

    public ServiceResponse<Void> increaseSessionCount() {

        ServiceResponse<Void> response = new ServiceResponse<>();

        sessionCount.incrementAndGet();

        return response;
    }

    public ServiceResponse<Void> decreaseSessionCount() {

        ServiceResponse<Void> response = new ServiceResponse<>();

        sessionCount.decrementAndGet();

        return response;
    }

    public ServiceResponse<Integer> getSessionCount() {

        ServiceResponse<Integer> response = new ServiceResponse<>();

        response.setDataObject(sessionCount.get());

        return response;
    }

    public ServiceResponse<Void> addLoggedOnUser(String username) {
        ServiceResponse<Void> response = new ServiceResponse<>();

        this.loggedOnUsers.add(username);

        return response;
    }

    public ServiceResponse<Void> removeLoggedOnUser(String username) {

        ServiceResponse<Void> response = new ServiceResponse<>();

        this.loggedOnUsers.remove(username);

        return response;
    }

    public ServiceResponse<Boolean> isUserLoggedOn(String username) {

        ServiceResponse<Boolean> response = new ServiceResponse<>();

        response.setDataObject(this.loggedOnUsers.contains(username));

        return response;
    }

    public ServiceResponse<Set<String>> getLoggedOnUsers() {

        ServiceResponse<Set<String>> response = new ServiceResponse<>();
        response.setDataObject(this.loggedOnUsers);
        return response;
    }


    /**
     * Refreshes the statistics data from the database
     * and updates the statistics field of this class.
     */
    public class Statistics {
        private CommentInfo lastComment;
        private AtomicLong commentCount = new AtomicLong();
        private AtomicLong discussionCount = new AtomicLong();
        private AtomicLong forumCount = new AtomicLong();
        private AtomicLong userCount = new AtomicLong();
        private AtomicLong forumGroupCount = new AtomicLong();
        private AtomicLong tagCount = new AtomicLong();
        private AtomicLong chatRoomCount = new AtomicLong();
        private String lastRegisteredUser;
        private Date lastUserRegisteredDate;

        public CommentInfo getLastComment() {
            return lastComment;
        }

        public void setLastComment(CommentInfo lastComment) {
            this.lastComment = lastComment;
        }

        //---
        public long getCommentCount() {
            return commentCount.get();
        }

        public void setCommentCount(long commentCount) {
            this.commentCount.set(commentCount);
        }

        public void addCommentCount(long value) {
            this.commentCount.addAndGet(value);
        }

        //---
        public long getDiscussionCount() {
            return discussionCount.get();
        }

        public void setDiscussionCount(Long discussionCount) {
            this.discussionCount.set(discussionCount);
        }

        public void addDiscussionCount(long value) {
            this.discussionCount.addAndGet(value);
        }

        //-----
        public long getForumCount() {
            return forumCount.get();
        }

        public void setForumCount(long forumCount) {
            this.forumCount.set(forumCount);
        }

        public void addForumCount(long value) {
            this.forumCount.addAndGet(value);
        }

        //---
        public long getUserCount() {
            return userCount.get();
        }

        public void setUserCount(long userCount) {
            this.userCount.set(userCount);
        }

        public void addUserCount(long value) {
            this.userCount.addAndGet(value);
        }
        //------

        public long getForumGroupCount() {
            return forumGroupCount.get();
        }

        public void setForumGroupCount(long forumGroupCount) {
            this.forumGroupCount.set(forumGroupCount);
        }

        public void addForumGroupCount(long value) {
            this.forumGroupCount.addAndGet(value);
        }

        // ------
        public long getTagCount() {
            return tagCount.get();
        }

        public void setTagCount(long tagCount) {
            this.tagCount.set(tagCount);
        }

        public void addTagCount(long value) {
            this.tagCount.addAndGet(value);
        }

        // ----------
        public long getChatRoomCount() {
            return chatRoomCount.get();
        }

        public void setChatRoomCount(long chatRoomCount) {
            this.chatRoomCount.set(chatRoomCount);
        }

        public void addChatRoomCount(long value) {
            this.chatRoomCount.addAndGet(value);
        }

        //----
        public String getLastRegisteredUser() {
            return lastRegisteredUser;
        }

        public void setLastRegisteredUser(String lastRegisteredUser) {
            this.lastRegisteredUser = lastRegisteredUser;
        }

        public Date getLastUserRegisteredDate() {
            return lastUserRegisteredDate;
        }

        public void setLastUserRegisteredDate(Date lastUserRegisteredDate) {
            this.lastUserRegisteredDate = lastUserRegisteredDate;
        }
    }
}
