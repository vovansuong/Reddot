package com.springboot.app.repository;

import com.springboot.app.forums.entity.CommentVote;
import com.springboot.app.forums.entity.Vote;
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

@Qualifier("voteDAO")
@Repository
public class VoteDAO {
    @PersistenceContext
    private EntityManager entityManager;

    public Vote getVote(CommentVote commentVote, String voteName) {
        String queryStr = "SELECT v FROM CommentVote cv, cv.votes v WHERE cv = :commentVote AND v.voterName = :voteName";
        TypedQuery<Vote> typedQuery = entityManager.createQuery(queryStr, Vote.class);
        typedQuery.setParameter("commentVote", commentVote);
        typedQuery.setParameter("voteName", voteName);
        List<Vote> resultList = typedQuery.getResultList();
        return resultList.isEmpty() ? null : resultList.getFirst();
    }

    /*
     * This method is used to get the reputation of all users in the system.
     */
    public Map<String, Long> getReputation4AllUsers() {
        Map<String, Long> results = new HashMap<>();

        String queryStr = "SELECT c.createdBy, COALESCE(SUM(v.voteValue), 0) FROM Comment c, c.commentVote.votes v GROUP BY c.createdBy";
        Query query = entityManager.createQuery(queryStr);

        @SuppressWarnings("unchecked")
        List<Object[]> resultList = query.getResultList();
        for (Object[] objArr : resultList) {
            results.put((String) objArr[0], (Long) (objArr[1]));
        }
        return results;
    }

//	public Number getReputation4User(String username) {
//		String queryStr = "SELECT COALESCE(SUM(v.voteValue), 0) FROM Comment c join c.commentVote.votes v WHERE c.createdBy = :username";
//		TypedQuery<Number> typedQuery = entityManager.createQuery(queryStr, Number.class);
//		typedQuery.setParameter("username", username);
//		return typedQuery.getSingleResult();
//	}

    public Number getReputation4User(String username) {
        String queryStr = "SELECT COALESCE(SUM(v.voteValue), 0) FROM Comment c JOIN c.commentVote cv JOIN cv.votes v WHERE c.createdBy = :username";
        TypedQuery<Number> typedQuery = entityManager.createQuery(queryStr, Number.class);
        typedQuery.setParameter("username", username);
        return typedQuery.getSingleResult();
    }


    public Map<String, Integer> getTopReputationUsers(LocalDateTime since, Integer maxResults) {
        Map<String, Integer> results = new HashMap<>();
        String queryStr = "SELECT c.createdBy, COALESCE(SUM(v.voteValue), 0) FROM Comment c, c.commentVote.votes v WHERE c.createdAt >= :since GROUP BY c.createdBy ORDER BY SUM(v.voteValue) DESC";
        Query query = entityManager.createQuery(queryStr);
        query.setParameter("since", since);
        query.setMaxResults(maxResults);

        @SuppressWarnings("unchecked")
        List<Object[]> resultList = query.getResultList();
        for (Object[] objArr : resultList) {
            results.put((String) objArr[0], (Integer) (objArr[1]));
        }
        return results;
    }
}
