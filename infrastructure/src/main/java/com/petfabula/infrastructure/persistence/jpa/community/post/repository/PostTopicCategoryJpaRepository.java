package com.petfabula.infrastructure.persistence.jpa.community.post.repository;

import com.petfabula.domain.aggregate.community.post.entity.PostTopicCategory;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostTopicCategoryJpaRepository extends JpaRepository<PostTopicCategory, Long> {

    @EntityGraph(value = "postTopicCategory.bare")
    PostTopicCategory findByTitle(String title);

    @EntityGraph(value = "postTopicCategory.all")
    List<PostTopicCategory> findAll();
}
