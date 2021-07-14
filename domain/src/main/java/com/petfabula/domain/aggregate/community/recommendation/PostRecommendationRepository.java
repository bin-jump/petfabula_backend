package com.petfabula.domain.aggregate.community.recommendation;

import com.petfabula.domain.aggregate.community.post.entity.Post;

public interface PostRecommendationRepository {

    PostRecommendation save(PostRecommendation postRecommendation);

    RecommendationResult<Post> findRandomRecommend(int page, int size, int seed, Long cursor);
}
