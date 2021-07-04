package com.petfabula.infrastructure.persistence.jpa.community.question.repository;

import com.petfabula.domain.aggregate.community.question.entity.AnswerCommentReply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AnswerCommentReplyJpaRepository extends JpaRepository<AnswerCommentReply, Long>, JpaSpecificationExecutor {
}
