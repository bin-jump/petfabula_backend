package com.petfabula.domain.aggregate.community.repository;

import com.petfabula.domain.aggregate.community.entity.PostCommentReply;
import com.petfabula.domain.common.paging.CursorPage;

public interface PostCommentReplyRepository {

    CursorPage<PostCommentReply> findByPostComment(Long postCommentId, Long cursor, int size);

    PostCommentReply findById(Long id);

    PostCommentReply save(PostCommentReply reply);

    void remove(PostCommentReply reply);
}
