package com.petfabula.domain.aggregate.community.recommendation;

import com.petfabula.domain.aggregate.community.question.entity.Question;
import com.petfabula.domain.common.paging.OffsetPage;

public interface QuestionRecommendationRepository {

    QuestionRecommendation save(QuestionRecommendation questionRecommendation);

    RecommendationResult<Question> findRandomRecommend(int page, int size, int seed, Long cursor);
}
