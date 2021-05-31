package com.petfabula.domain.aggregate.community.repository;

import com.petfabula.domain.aggregate.community.entity.PostComment;
import com.petfabula.domain.common.paging.CursorPage;

public interface PostCommentRepository {

    CursorPage<PostComment> findByPostId(Long postId, Long cursor, int size);

    PostComment findById(Long id);

    PostComment save(PostComment postComment);

    void remove(PostComment postComment);
}
