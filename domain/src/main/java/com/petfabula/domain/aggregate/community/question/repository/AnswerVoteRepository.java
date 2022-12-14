package com.petfabula.domain.aggregate.community.question.repository;

import com.petfabula.domain.aggregate.community.question.entity.valueobject.UpvoteAnswer;
import com.petfabula.domain.aggregate.community.question.entity.valueobject.UpvoteAnswerId;

import java.util.List;

public interface AnswerVoteRepository {

    UpvoteAnswer save(UpvoteAnswer upvoteAnswer);

    UpvoteAnswer find(Long participatorId, Long answerId);

    List<UpvoteAnswer> findByParticipatorIdVoted(Long participatorId, List<Long> answerIds);

    void remove(UpvoteAnswer upvoteAnswer);
}
