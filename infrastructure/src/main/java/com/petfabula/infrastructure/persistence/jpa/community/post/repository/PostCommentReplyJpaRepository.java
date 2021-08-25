package com.petfabula.infrastructure.persistence.jpa.community.post.repository;

import com.petfabula.domain.aggregate.community.post.entity.PostCommentReply;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.Optional;

public interface PostCommentReplyJpaRepository extends JpaRepository<PostCommentReply, Long>, JpaSpecificationExecutor {

    @EntityGraph(value = "postCommentReply.all")
    @Query("select r from PostCommentReply r where r.id = :id")
    Optional<PostCommentReply> findById(Long id);

    @EntityGraph(value = "postCommentReply.all")
    @Override
    Page<PostCommentReply> findAll(Specification var1, Pageable var2);

    @EntityGraph(value = "postComment.all")
    List<PostCommentReply> findByIdIn(List<Long> ids);
}
