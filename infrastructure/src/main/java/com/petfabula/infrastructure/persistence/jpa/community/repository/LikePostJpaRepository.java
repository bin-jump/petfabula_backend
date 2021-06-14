package com.petfabula.infrastructure.persistence.jpa.community.repository;

import com.petfabula.domain.aggregate.community.post.entity.valueobject.LikePost;
import com.petfabula.domain.aggregate.community.post.entity.valueobject.LikePostId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LikePostJpaRepository extends JpaRepository<LikePost, LikePostId> {

    @Query("select lp from LikePost lp where lp.likePostId.participatorId = :participatorId " +
            " and lp.likePostId.postId = :postId")
    LikePost find(@Param("participatorId")Long participatorId, @Param("postId")Long postId);
}
