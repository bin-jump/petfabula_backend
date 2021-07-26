package com.petfabula.domain.aggregate.community.post.repository;

import com.petfabula.domain.aggregate.community.post.entity.Post;
import com.petfabula.domain.common.paging.CursorPage;

import java.util.List;

public interface PostRepository {

    CursorPage<Post> findByParticipatorId(Long participatorId, Long cursor, int size);

    List<Post> findByIds(List<Long> ids);

    Post save(Post post);

    Post findById(Long id);

    void remove(Post post);
}
