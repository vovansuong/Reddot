package com.springboot.app.forums.service.impl;

import com.springboot.app.accounts.entity.UserStat;
import com.springboot.app.accounts.repository.UserStatRepository;
import com.springboot.app.dto.response.AckCodeType;
import com.springboot.app.dto.response.ServiceResponse;
import com.springboot.app.forums.dto.request.CommentVoteRequest;
import com.springboot.app.forums.entity.Comment;
import com.springboot.app.forums.entity.CommentVote;
import com.springboot.app.forums.entity.Vote;
import com.springboot.app.forums.repository.CommentRepository;
import com.springboot.app.forums.repository.CommentVoteRepository;
import com.springboot.app.forums.repository.VoteRepository;
import com.springboot.app.forums.service.VoteService;
import com.springboot.app.repository.StatDAO;
import com.springboot.app.repository.VoteDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@Service
public class VoteServiceImpl implements VoteService {
    @Autowired
    private VoteRepository voteRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private CommentVoteRepository commentVoteRepository;
    @Autowired
    private UserStatRepository userStatRepository;

    @Autowired
    private VoteDAO voteDAO;

    @Autowired
    private StatDAO statDAO;

    @Transactional(readOnly = false)
    public ServiceResponse<Void> registerCommentVote(CommentVoteRequest commentVoteRequest, String voteName, Short voteValue) {
        ServiceResponse<Void> response = new ServiceResponse<>();
        Comment comment = commentRepository.findById(commentVoteRequest.getCommentId()).orElse(null);
        if (comment == null) {
            response.addMessage("Comment not found");
            response.setAckCode(AckCodeType.FAILURE);
            return response;
        }
        if (comment.getCreatedBy().equals(voteName)) {
            response.addMessage("User cannot vote on own comment");
            response.setAckCode(AckCodeType.FAILURE);
            return response;
        }

        CommentVote commentVote = comment.getCommentVote();
        Vote vote = getVote(commentVote, voteName);

        if (vote == null) {
            vote = new Vote();
            vote.setVoterName(voteName);
            vote.setVoteValue(voteValue);
            voteRepository.save(vote);

            //update commentVote
            commentVote.getVotes().add(vote);
            if (voteValue == 10) {
                commentVote.setVoteDownCount(commentVote.getVoteUpCount() + 1);
                addReputationAfterVote(comment, 10);
            } else if (voteValue == -2) {
                commentVote.setVoteDownCount(commentVote.getVoteDownCount() + 1);
                addReputationAfterVote(comment, -2);
            }
            commentVoteRepository.save(commentVote);

            response.addMessage("Vote on comment registered successfully for voter " + voteName);
        } else if (vote.getVoteValue() != voteValue) {
            // update commentVote count and reputation of user who voted
            if (voteValue == 10) {
                commentVote.setVoteDownCount(commentVote.getVoteDownCount() - 1);
                commentVote.setVoteUpCount(commentVote.getVoteUpCount() + 1);
                addReputationAfterVote(comment, 2);
            } else if (voteValue == -2) {
                commentVote.setVoteUpCount(commentVote.getVoteUpCount() - 1);
                commentVote.setVoteDownCount(commentVote.getVoteDownCount() + 1);
                addReputationAfterVote(comment, -10);
            }
            commentVote.getVotes().remove(vote);
            commentVoteRepository.save(commentVote);
            voteRepository.delete(vote);

            response.addMessage("Vote on comment updated successfully for voter " + voteName);
        } else {
            response.addMessage("Voter already voted on the comment");
            response.setAckCode(AckCodeType.FAILURE);
        }
        return response;
    }

    public Vote getVote(CommentVote commentVote, String voteName) {
        Optional<Vote> vote = commentVote.getVotes().stream().filter(v -> v.getVoterName().equals(voteName)).findFirst();
        return vote.orElse(null);
    }

    @Transactional(readOnly = false)
    public ServiceResponse<Map<String, Long>> getReputation4AllUsers() {
        ServiceResponse<Map<String, Long>> response = new ServiceResponse<>();
        response.setDataObject(voteDAO.getReputation4AllUsers());
        return response;
    }

    @Transactional(readOnly = false)
    public ServiceResponse<Map<String, Integer>> getMostReputationUsers(LocalDateTime sinceDate, Integer limit) {
        ServiceResponse<Map<String, Integer>> response = new ServiceResponse<>();
        response.setDataObject(voteDAO.getTopReputationUsers(sinceDate, limit));
        return response;
    }


    public void addReputationAfterVote(Comment comment, int voteValue) {
        String username = comment.getCreatedBy();
        UserStat stat = statDAO.getUserStat(username);
        stat.addReputation(voteValue);
        userStatRepository.save(stat);
    }
}
