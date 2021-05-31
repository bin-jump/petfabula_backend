package com.petfabula.infrastructure.persistence.jpa.community.repository;

import com.petfabula.domain.aggregate.community.entity.PostTopicRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PostTopicRelationJpaRepository extends JpaRepository<PostTopicRelation, Long>, JpaSpecificationExecutor {
}
