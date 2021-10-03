package com.petfabula.infrastructure.persistence.jpa.community.post.repository;

import com.petfabula.domain.aggregate.community.post.entity.PostTopic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostTopicJpaRepository extends JpaRepository<PostTopic, Long> {

    PostTopic findByTitle(String title);

    @Modifying(flushAutomatically = true)
    @Query("update PostTopic t set t.topicCategoryTitle = :title where t.topicCategoryId = :categoryId")
    void updateCategoryTitle(@Param("categoryId") Long categoryId, @Param("title") String title);

    @Modifying(flushAutomatically = true)
    @Query("delete from PostTopic t where t.topicCategoryId = :categoryId")
    void removeByCategoryId(@Param("categoryId") Long categoryId);
}
