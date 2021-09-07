package com.petfabula.domain.aggregate.community.recommendation;

import com.petfabula.domain.aggregate.community.post.entity.Post;

public interface PostRecommendationRepository {

    PostRecommendation save(PostRecommendation postRecommendation);

    PostRecommendation findByPostId(Long postId);

    RecommendationResult<Post> findRandomRecommend(int page, int size, int seed, Long cursor);

    void remove(PostRecommendation postRecommendation);
}
