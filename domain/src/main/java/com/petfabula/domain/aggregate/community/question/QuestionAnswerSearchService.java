package com.petfabula.domain.aggregate.community.question;

import com.petfabula.domain.common.search.SearchAfterResult;
import com.petfabula.domain.common.search.SearchQueryRequest;

public interface QuestionAnswerSearchService {

    void index(QuestionAnswerSearchItem questionAnswerSearchItem);

    SearchAfterResult<QuestionAnswerSearchItem, Long> search(SearchQueryRequest searchQueryRequest);

    void remove(Long id);
}
