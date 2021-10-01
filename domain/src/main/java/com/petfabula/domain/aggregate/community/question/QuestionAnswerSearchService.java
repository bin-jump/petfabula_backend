package com.petfabula.domain.aggregate.community.question;

import com.petfabula.domain.common.search.SearchAfterResult;
import com.petfabula.domain.common.search.SearchQueryRequest;

public interface QuestionAnswerSearchService {

    void index(QuestionAnswerSearchItem questionAnswerSearchItem);

    SearchAfterResult<QuestionAnswerSearchItem, Long> search(SearchQueryRequest searchQueryRequest);

    void updateAnswerQuestionTitle(Long questionId, String title);

    void updateParticipatorInfo(Long participatorId, String photo);

    void removeByQuestionId(Long questionId);

    void removeAnswerByAnswerId(Long answerId);

    void removeQuestionByQuestionId(Long questionId);

}
