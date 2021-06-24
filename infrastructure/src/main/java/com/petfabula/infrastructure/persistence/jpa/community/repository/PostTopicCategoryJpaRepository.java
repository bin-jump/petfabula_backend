package com.petfabula.infrastructure.persistence.jpa.community.repository;

import com.petfabula.domain.aggregate.community.post.entity.valueobject.PostTopicCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostTopicCategoryJpaRepository extends JpaRepository<PostTopicCategory, Long> {

    PostTopicCategory findByTitle(String title);
}
