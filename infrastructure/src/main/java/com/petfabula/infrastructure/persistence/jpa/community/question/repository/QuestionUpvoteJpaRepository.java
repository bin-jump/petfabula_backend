package com.petfabula.infrastructure.persistence.jpa.community.question.repository;

import com.petfabula.domain.aggregate.community.question.entity.valueobject.UpvoteQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionUpvoteJpaRepository extends JpaRepository<UpvoteQuestion, Long> {

    UpvoteQuestion findByParticipatorIdAndQuestionId(Long participatorId, Long questionId);
}
