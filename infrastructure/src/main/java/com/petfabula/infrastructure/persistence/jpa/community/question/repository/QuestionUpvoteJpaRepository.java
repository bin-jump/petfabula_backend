package com.petfabula.infrastructure.persistence.jpa.community.question.repository;

import com.petfabula.domain.aggregate.community.question.entity.valueobject.UpvoteQuestion;
import com.petfabula.domain.aggregate.community.question.entity.valueobject.UpvoteQuestionId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionUpvoteJpaRepository extends JpaRepository<UpvoteQuestion, UpvoteQuestionId> {

}
