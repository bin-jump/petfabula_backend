package com.petfabula.domain.aggregate.community.repository;

import com.petfabula.domain.aggregate.community.entity.LikePost;

public interface LikePostRepository {

    LikePost find(Long participatorId, Long postId);

    LikePost save(LikePost likePost);

    void remove(LikePost likePost);
}
