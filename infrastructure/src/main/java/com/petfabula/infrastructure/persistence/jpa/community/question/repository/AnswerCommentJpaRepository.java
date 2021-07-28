package com.petfabula.infrastructure.persistence.jpa.community.question.repository;

import com.petfabula.domain.aggregate.community.question.entity.AnswerComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface AnswerCommentJpaRepository extends JpaRepository<AnswerComment, Long>, JpaSpecificationExecutor {

    List<AnswerComment> findByIdIn(List<Long> ids);
}
