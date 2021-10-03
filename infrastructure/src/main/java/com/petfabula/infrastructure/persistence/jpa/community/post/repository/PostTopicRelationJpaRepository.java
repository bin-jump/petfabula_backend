package com.petfabula.infrastructure.persistence.jpa.community.post.repository;

import com.petfabula.domain.aggregate.community.post.entity.valueobject.PostTopicRelation;
import com.petfabula.domain.aggregate.community.post.entity.valueobject.PostTopicRelationId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostTopicRelationJpaRepository extends JpaRepository<PostTopicRelation, PostTopicRelationId>, JpaSpecificationExecutor {

    PostTopicRelation findByPostId(Long postId);

    @Modifying(flushAutomatically = true)
    @Query("delete from PostTopicRelation r where r.topicId = :topicId")
    void removeByTopicId(@Param("topicId") Long topicId);

    @Modifying(flushAutomatically = true)
    @Query("delete from PostTopicRelation r where r.postId = :postId")
    void removeByPostId(@Param("postId") Long postId);

    @Modifying(flushAutomatically = true)
    @Query("delete from PostTopicRelation r where r.topicCategoryId = :topicCategoryId")
    void removeByTopicCategoryId(@Param("topicCategoryId") Long topicCategoryId);
}
