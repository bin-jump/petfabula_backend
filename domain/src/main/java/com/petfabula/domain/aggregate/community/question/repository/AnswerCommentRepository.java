package com.petfabula.domain.aggregate.community.question.repository;

import com.petfabula.domain.aggregate.community.question.entity.AnswerComment;
import com.petfabula.domain.common.paging.CursorPage;

import java.util.List;

public interface AnswerCommentRepository {

    CursorPage<AnswerComment> findByAnswerId(Long answerId, Long cursor, int size);

    List<AnswerComment> findByIds(List<Long> ids);

    AnswerComment findById(Long id);

    AnswerComment save(AnswerComment answerComment);

    void remove(AnswerComment answerComment);
}
