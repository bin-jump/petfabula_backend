package com.petfabula.infrastructure.persistence.jpa.community.question.impl;

import com.petfabula.domain.aggregate.community.question.entity.valueobject.UpvoteQuestion;
import com.petfabula.domain.aggregate.community.question.repository.QuestionVoteRepository;
import com.petfabula.infrastructure.persistence.jpa.community.question.repository.QuestionUpvoteJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class QuestionVoteRepositoryImpl implements QuestionVoteRepository {

    @Autowired
    private QuestionUpvoteJpaRepository questionUpvoteJpaRepository;

    @Override
    public UpvoteQuestion save(UpvoteQuestion upvoteQuestion) {
        return questionUpvoteJpaRepository.save(upvoteQuestion);
    }

    @Override
    public UpvoteQuestion find(Long participatorId, Long questionId) {
        return questionUpvoteJpaRepository.findByParticipatorIdAndQuestionId(participatorId, questionId);
    }

    @Override
    public void remove(UpvoteQuestion upvoteQuestion) {
        questionUpvoteJpaRepository.delete(upvoteQuestion);
    }
}
