package com.springboot.app.repository;

import com.springboot.app.accounts.entity.UserStat;
import com.springboot.app.forums.entity.Comment;
import com.springboot.app.forums.entity.CommentInfo;
import com.springboot.app.forums.entity.Discussion;
import com.springboot.app.forums.entity.Forum;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Number countThumbnails(Discussion discussion);
 * Map<String, Integer> getCommentorMap(Discussion discussion);
 * Comment findLatestCommentByDiscussion(Discussion discussion);
 */
@Qualifier("statDAO")
@Repository
public class StatDAO {
    @PersistenceContext
    private EntityManager entityManager;

    public Number countThumbnailsByDiscussion(Discussion discussion) {
        String nativeQuery = "SELECT COUNT(1) FROM comment_thumbnail CT"
                             + " LEFT JOIN Comment C ON CT.comment_id = C.ID"
                             + " WHERE C.discussion_id = ?1";

        Query query = entityManager.createNativeQuery(nativeQuery).setParameter(1, discussion.getId());

        return (Number) query.getSingleResult();
    }

    /**
     * This method identical as the method above,
     * using native SQL query to avoid SQL Server issue as noted above
     */
    public Number countAttachmentsByDiscussion(Discussion discussion) {

        String nativeQuery = "SELECT COUNT(1) FROM comment_attachment CA"
                             + " LEFT JOIN comment C ON CA.comment_id = C.id"
                             + " WHERE C.discussion_id = ?1";

        Query query = entityManager.createNativeQuery(nativeQuery).setParameter(1, discussion.getId());

        /*
         * Note: the query above returns Long in Postgresql and BigInteger in SQL Server
         * So, the compromise is to downcast to Number first
         */
        return (Number) query.getSingleResult();
    }

    public Map<String, Integer> getCommentorMap(Discussion discussion) {

        Map<String, Integer> commentors = new HashMap<>();

        Query query = entityManager.createQuery("SELECT c.createdBy, count(c) FROM Comment c WHERE c.discussion = :discussion"
                                                + " GROUP BY c.createdBy");
        query.setParameter("discussion", discussion);

        @SuppressWarnings("unchecked")
        List<Object[]> resultList = query.getResultList();

        for (Object[] objectArray : resultList) {
            commentors.put((String) objectArray[0], ((Number) objectArray[1]).intValue());
        }

        return commentors;
    }

    public Comment findLatestCommentByDiscussion(Discussion discussion) {
        TypedQuery<Comment> typedQuery = entityManager.createQuery("SELECT c FROM Comment c WHERE c.discussion = :discussion ORDER BY c.id DESC", Comment.class);
        typedQuery.setParameter("discussion", discussion);

        List<Comment> resultList = typedQuery.setMaxResults(1).getResultList();

        return resultList.isEmpty() ? null : resultList.get(0);
    }

    public UserStat getUserStat(String username) {

        TypedQuery<UserStat> typedQuery = entityManager.createQuery("SELECT u.stat FROM User u where u.username = :username", UserStat.class);
        typedQuery.setParameter("username", username);

        List<UserStat> resultList = typedQuery.setMaxResults(1).getResultList();

        return resultList.isEmpty() ? null : resultList.get(0);
    }


    public Number countCommentByDiscussion(Discussion discussion) {
        TypedQuery<Number> typedQuery = entityManager.createQuery("SELECT COUNT(c) FROM Comment c WHERE c.discussion = :discussion", Number.class);
        typedQuery.setParameter("discussion", discussion);
        return typedQuery.getSingleResult();
    }

    public Number countCommentByForum(Forum forum) {
        TypedQuery<Number> typedQuery = entityManager.createQuery("SELECT COUNT(c) FROM Comment c WHERE c.discussion.forum = :forum", Number.class);
        typedQuery.setParameter("forum", forum);
        return typedQuery.getSingleResult();
    }

    public Comment latestCommentByForum(Forum forum) {
        TypedQuery<Comment> typedQuery = entityManager.createQuery("SELECT c FROM Comment c WHERE c.discussion.forum = :forum ORDER BY c.id DESC", Comment.class);
        typedQuery.setParameter("forum", forum);
        List<Comment> resultList = typedQuery.setMaxResults(1).getResultList();
        return resultList.isEmpty() ? null : resultList.getFirst();
    }

    public CommentInfo latestCommentInfoByForum(Forum forum) {
        TypedQuery<CommentInfo> typedQuery = entityManager.createQuery("SELECT d.stat.lastComment FROM Discussion d WHERE d.forum = :forum ORDER BY d.stat.lastComment.commentDate DESC", CommentInfo.class);
        typedQuery.setParameter("forum", forum);
        List<CommentInfo> resultList = typedQuery.setMaxResults(1).getResultList();
        return resultList.isEmpty() ? null : resultList.getFirst();
    }

    /*
     * A note about TypedQuery.getSingleResult() vs.Typedquery.getResultList(0):
     *  The getSingleResult() method throws NonUniqueResultException
     *  refs:
     *  	- https://stackoverflow.com/questions/8500031/what-is-better-getsingleresult-or-getresultlist-jpa
     *  	- http://sysout.be/2011/03/09/why-you-should-never-use-getsingleresult-in-jpa/
     */
    public CommentInfo lastCommentInfo() {
        TypedQuery<CommentInfo> typedQuery = entityManager.createQuery("SELECT c FROM CommentInfo c ORDER BY c.commentDate DESC", CommentInfo.class);
        List<CommentInfo> resultList = typedQuery.setMaxResults(1).getResultList();
        return resultList.isEmpty() ? null : resultList.getFirst();
    }

    public Map<String, Integer> getMostVotedUpUsers(LocalDateTime since, Integer maxResult) {

        Map<String, Integer> users = new HashMap<>();

        Query query = entityManager.createQuery("SELECT c.createBy, COALESCE(SUM(c.commentVote.voteUpCount), 0) voteUpCount"
                                                + " FROM Comment c WHERE c.createdAt >= :since GROUP BY c.createdBy ORDER BY voteUpCount DESC");

        query.setParameter("since", since);

        if (maxResult != null) {
            query.setMaxResults(maxResult);
        }

        @SuppressWarnings("unchecked")
        List<Object[]> resultList = query.getResultList();

        for (Object[] objectArray : resultList) {
            users.put((String) objectArray[0], ((Number) objectArray[1]).intValue());
        }

        return users;
    }

    public Number countComment(String username) {
        TypedQuery<Number> typedQuery = entityManager.createQuery("SELECT COUNT(c) FROM Comment c WHERE c.createdBy = :username", Number.class);
        typedQuery.setParameter("username", username);

        return typedQuery.getSingleResult();
    }

    public Number countDiscussion(String username) {

        TypedQuery<Number> typedQuery = entityManager.createQuery("SELECT COUNT(d) FROM Discussion d WHERE d.createdBy = :username", Number.class);
        typedQuery.setParameter("username", username);

        return typedQuery.getSingleResult();
    }

    public Comment getLatestComment(Discussion discussion) {
        TypedQuery<Comment> typedQuery = entityManager.createQuery("SELECT c FROM Comment c WHERE c.discussion = :discussion ORDER BY c.id DESC", Comment.class);
        typedQuery.setParameter("discussion", discussion);

        List<Comment> resultList = typedQuery.setMaxResults(1).getResultList();

        return resultList.isEmpty() ? null : resultList.get(0);
    }

}

