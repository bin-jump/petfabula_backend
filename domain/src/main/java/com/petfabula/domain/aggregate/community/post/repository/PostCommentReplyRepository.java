package com.petfabula.domain.aggregate.community.post.repository;

import com.petfabula.domain.aggregate.community.post.entity.PostCommentReply;
import com.petfabula.domain.common.paging.CursorPage;

import java.util.List;

public interface PostCommentReplyRepository {

    CursorPage<PostCommentReply> findByPostComment(Long postCommentId, Long cursor, int size);

    PostCommentReply findById(Long id);

    List<PostCommentReply> findByIds(List<Long> ids);

    PostCommentReply save(PostCommentReply reply);

    void remove(PostCommentReply reply);
}
