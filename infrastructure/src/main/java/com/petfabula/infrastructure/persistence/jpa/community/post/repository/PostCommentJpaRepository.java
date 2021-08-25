package com.petfabula.infrastructure.persistence.jpa.community.post.repository;

import com.petfabula.domain.aggregate.community.post.entity.PostComment;
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

public interface PostCommentJpaRepository extends JpaRepository<PostComment, Long>, JpaSpecificationExecutor {

    @EntityGraph(value = "postComment.all")
    @Query("select c from PostComment c where c.id = :id")
    Optional<PostComment> findById(Long id);

    @EntityGraph(value = "postComment.all")
    @Override
    Page<PostComment> findAll(@Nullable Specification var1, Pageable var2);

    @EntityGraph(value = "postComment.all")
    List<PostComment> findByIdIn(List<Long> ids);
}
