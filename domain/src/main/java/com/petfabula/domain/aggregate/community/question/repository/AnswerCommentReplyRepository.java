package com.petfabula.domain.aggregate.community.question.repository;

import com.petfabula.domain.aggregate.community.question.entity.AnswerCommentReply;
import com.petfabula.domain.common.paging.CursorPage;

public interface AnswerCommentReplyRepository {

    CursorPage<AnswerCommentReply> findByComment(Long answerCommentId, Long cursor, int size);

    AnswerCommentReply findById(Long id);

    AnswerCommentReply save(AnswerCommentReply reply);

    void remove(AnswerCommentReply reply);
}
