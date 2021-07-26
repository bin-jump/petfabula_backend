package com.petfabula.infrastructure.persistence.jpa.community.post.repository;

import com.petfabula.domain.aggregate.community.post.entity.valueobject.CollectPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CollectPostJpaRepository extends JpaRepository<CollectPost, Long>, JpaSpecificationExecutor {

    CollectPost findByParticipatorIdAndPostId(Long participatorId, Long postId);
}
