package com.petfabula.domain.aggregate.community.post.repository;

import com.petfabula.domain.aggregate.community.post.entity.valueobject.CollectPost;

public interface CollectPostRepository {

    CollectPost save(CollectPost collectPost);

    CollectPost find(Long participatorId, Long postId);

    void remove(CollectPost likePost);
}
