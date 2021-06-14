package com.petfabula.domain.aggregate.community.post.repository;

import com.petfabula.domain.aggregate.community.post.entity.valueobject.LikePost;

public interface LikePostRepository {

    LikePost find(Long participatorId, Long postId);

    LikePost save(LikePost likePost);

    void remove(LikePost likePost);
}
