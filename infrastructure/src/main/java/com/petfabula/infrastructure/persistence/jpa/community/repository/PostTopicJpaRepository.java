package com.petfabula.infrastructure.persistence.jpa.community.repository;

import com.petfabula.domain.aggregate.community.post.entity.PostTopic;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostTopicJpaRepository extends JpaRepository<PostTopic, Long> {

    PostTopic findByTitle(String title);
}
