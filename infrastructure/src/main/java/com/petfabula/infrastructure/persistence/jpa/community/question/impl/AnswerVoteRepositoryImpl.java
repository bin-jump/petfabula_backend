package com.petfabula.infrastructure.persistence.jpa.community.question.impl;

import com.petfabula.domain.aggregate.community.question.entity.valueobject.UpvoteAnswer;
import com.petfabula.domain.aggregate.community.question.repository.AnswerVoteRepository;
import com.petfabula.infrastructure.persistence.jpa.community.question.repository.AnswerUpvoteJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AnswerVoteRepositoryImpl implements AnswerVoteRepository {

    @Autowired
    private AnswerUpvoteJpaRepository answerUpvoteJpaRepository;

    @Override
    public UpvoteAnswer save(UpvoteAnswer upvoteAnswer) {
        return answerUpvoteJpaRepository.save(upvoteAnswer);
    }

    @Override
    public UpvoteAnswer find(Long participatorId, Long answerId) {
        return answerUpvoteJpaRepository.findByParticipatorIdAndAnswerId(participatorId, answerId);
    }

    @Override
    public List<UpvoteAnswer> findByParticipatorIdVoted(Long participatorId, List<Long> answerIds) {
        return answerUpvoteJpaRepository.findByParticipatorIdAndAnswerIdIn(participatorId, answerIds);
    }

    @Override
    public void remove(UpvoteAnswer upvoteAnswer) {
        answerUpvoteJpaRepository.delete(upvoteAnswer);
    }
}
