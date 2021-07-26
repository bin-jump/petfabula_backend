package com.petfabula.domain.aggregate.community.post.repository;

import com.petfabula.domain.aggregate.community.post.entity.Post;
import com.petfabula.domain.aggregate.community.post.entity.valueobject.CollectPost;
import com.petfabula.domain.common.paging.CursorPage;

public interface CollectPostRepository {

    CursorPage<Post> findByParticipatorId(Long participatorId, Long cursor, int size);

    CollectPost save(CollectPost collectPost);

    CollectPost find(Long participatorId, Long postId);

    void remove(CollectPost likePost);
}
