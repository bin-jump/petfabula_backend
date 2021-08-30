package com.petfabula.domain.aggregate.community.question.repository;

import com.petfabula.domain.aggregate.community.question.entity.Question;
import com.petfabula.domain.common.paging.CursorPage;

import java.util.List;

public interface QuestionRepository {

    Question save(Question question);

    Question findById(Long id);

    List<Question> findByIds(List<Long> ids);

    void remove(Question question);

    CursorPage<Question> findByParticipatorId(Long participatorId, Long cursor, int size);

    CursorPage<Question> findRecent(Long cursor, int size);
}
