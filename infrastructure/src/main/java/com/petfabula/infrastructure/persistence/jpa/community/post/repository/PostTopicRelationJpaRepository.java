package com.petfabula.infrastructure.persistence.jpa.community.post.repository;

import com.petfabula.domain.aggregate.community.post.entity.valueobject.PostTopicRelation;
import com.petfabula.domain.aggregate.community.post.entity.valueobject.PostTopicRelationId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PostTopicRelationJpaRepository extends JpaRepository<PostTopicRelation, PostTopicRelationId>, JpaSpecificationExecutor {

    PostTopicRelation findByPostId(Long postId);
}
