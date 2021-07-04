package com.petfabula.domain.aggregate.community.question.repository;

import com.petfabula.domain.aggregate.community.question.entity.Answer;
import com.petfabula.domain.common.paging.CursorPage;

public interface AnswerRepository {

    Answer save(Answer answer);

    Answer findById(Long answerId);

    void remove(Answer answer);

    CursorPage<Answer> findRecent(Long cursor, int size);

    CursorPage<Answer> findByQuestionId(Long questionId, Long cursor, int size);

    CursorPage<Answer> findByParticipatorId(Long participatorId, Long cursor, int size);
}
