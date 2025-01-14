package com.springboot.app.forums.service;

import com.springboot.app.dto.response.ServiceResponse;
import com.springboot.app.forums.dto.request.CommentVoteRequest;
import com.springboot.app.forums.entity.CommentVote;
import com.springboot.app.forums.entity.Vote;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;

@Component
public interface VoteService {
    ServiceResponse<Void> registerCommentVote(CommentVoteRequest comment, String voteName, Short voteValue);

    Vote getVote(CommentVote commentVote, String voteName);

    ServiceResponse<Map<String, Long>> getReputation4AllUsers();

    ServiceResponse<Map<String, Integer>> getMostReputationUsers(LocalDateTime sinceDate, Integer limit);

}
