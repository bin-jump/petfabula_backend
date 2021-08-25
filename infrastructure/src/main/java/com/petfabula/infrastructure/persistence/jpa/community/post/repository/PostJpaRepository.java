package com.petfabula.infrastructure.persistence.jpa.community.post.repository;

import com.petfabula.domain.aggregate.community.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.Optional;

public interface PostJpaRepository extends JpaRepository<Post, Long>, JpaSpecificationExecutor {

    @EntityGraph(value = "post.all")
    List<Post> findByIdInOrderByIdDesc(List<Long> ids);

    @EntityGraph(value = "post.all")
    @Query("select p from Post p where p.id = :id")
    Optional<Post> findById(Long id);

}
