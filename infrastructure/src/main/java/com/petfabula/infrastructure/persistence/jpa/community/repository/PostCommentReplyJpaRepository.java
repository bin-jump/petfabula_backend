package com.petfabula.infrastructure.persistence.jpa.community.repository;

import com.petfabula.domain.aggregate.community.entity.PostCommentReply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PostCommentReplyJpaRepository extends JpaRepository<PostCommentReply, Long>, JpaSpecificationExecutor {
}
