package com.petfabula.domain.aggregate.community.post.repository;

import com.petfabula.domain.aggregate.community.post.entity.PostComment;
import com.petfabula.domain.common.paging.CursorPage;

import java.util.List;

public interface PostCommentRepository {

    CursorPage<PostComment> findByPostId(Long postId, Long cursor, int size);

    PostComment findById(Long id);

    List<PostComment> findByIds(List<Long> ids);

    PostComment save(PostComment postComment);

    void remove(PostComment postComment);
}
