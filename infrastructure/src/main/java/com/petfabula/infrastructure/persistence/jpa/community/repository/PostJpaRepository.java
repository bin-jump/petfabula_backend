package com.petfabula.infrastructure.persistence.jpa.community.repository;

import com.petfabula.domain.aggregate.community.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PostJpaRepository extends JpaRepository<Post, Long>, JpaSpecificationExecutor {
}
