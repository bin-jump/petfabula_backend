package com.petfabula.infrastructure.persistence.jpa.recommendation;

import com.petfabula.domain.aggregate.community.recommendation.QuestionRecommendation;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRecommendationJpaRepository extends JpaRepository<QuestionRecommendation, Long> {

    @EntityGraph(value = "questionRecommendation.bare")
    QuestionRecommendation findTopByOrderByIdDesc();
}
