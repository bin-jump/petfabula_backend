package com.petfabula.domain.aggregate.community.question.repository;

import com.petfabula.domain.aggregate.community.question.entity.valueobject.UpvoteQuestion;

public interface QuestionVoteRepository {

    UpvoteQuestion save(UpvoteQuestion upvoteQuestion);

    UpvoteQuestion find(Long participatorId, Long questionId);

    void remove(UpvoteQuestion upvoteQuestion);
}
