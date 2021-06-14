package com.petfabula.domain.aggregate.community.post.repository;

import com.petfabula.domain.aggregate.community.post.entity.Post;
import com.petfabula.domain.common.paging.CursorPage;

public interface PostRepository {

    CursorPage<Post> findByParticipatorId(Long participatorId, Long cursor, int size);

    Post save(Post post);

    Post findById(Long id);

    void remove(Post post);
}
