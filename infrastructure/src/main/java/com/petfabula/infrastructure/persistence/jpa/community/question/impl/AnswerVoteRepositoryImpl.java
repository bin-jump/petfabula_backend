package com.petfabula.infrastructure.persistence.jpa.community.question.impl;

import com.petfabula.domain.aggregate.community.question.entity.valueobject.UpvoteAnswer;
import com.petfabula.domain.aggregate.community.question.entity.valueobject.UpvoteAnswerId;
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
        UpvoteAnswerId id = new UpvoteAnswerId(participatorId, answerId);
        return answerUpvoteJpaRepository.findById(id).orElse(null);
    }

    @Override
    public List<UpvoteAnswer> findByIds(List<UpvoteAnswerId> ids) {
        return answerUpvoteJpaRepository.findAllById(ids);
    }


    @Override
    public void remove(UpvoteAnswer upvoteAnswer) {
        answerUpvoteJpaRepository.delete(upvoteAnswer);
    }
}
