package com.petfabula.infrastructure.persistence.jpa.community.post.repository;

import com.petfabula.domain.aggregate.community.post.entity.valueobject.LikePost;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikePostJpaRepository extends JpaRepository<LikePost, Long> {

    LikePost findByParticipatorIdAndPostId(Long participatorId, Long postId);
}
