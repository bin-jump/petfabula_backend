package com.petfabula.infrastructure.persistence.jpa.community.question.repository;

import com.petfabula.domain.aggregate.community.question.entity.valueobject.UpvoteAnswer;
import com.petfabula.domain.aggregate.community.question.entity.valueobject.UpvoteAnswerId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnswerUpvoteJpaRepository extends JpaRepository<UpvoteAnswer, UpvoteAnswerId> {

    List<UpvoteAnswer> findByAnswerIdIn(List<Long> answerIds);
}
