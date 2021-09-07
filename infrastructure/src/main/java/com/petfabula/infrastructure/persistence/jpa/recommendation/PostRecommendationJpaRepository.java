package com.petfabula.infrastructure.persistence.jpa.recommendation;

import com.petfabula.domain.aggregate.community.recommendation.PostRecommendation;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRecommendationJpaRepository extends JpaRepository<PostRecommendation, Long> {

    @EntityGraph(value = "postRecommendation.bare")
    PostRecommendation findTopByOrderByIdDesc();

    @EntityGraph(value = "postRecommendation.bare")
    PostRecommendation findByPostId(Long postId);
}
