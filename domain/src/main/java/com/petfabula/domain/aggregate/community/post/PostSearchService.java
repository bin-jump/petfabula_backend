package com.petfabula.domain.aggregate.community.post;

import com.petfabula.domain.common.search.SearchAfterResult;
import com.petfabula.domain.common.search.SearchQueryRequest;

public interface PostSearchService {

    void index(PostSearchItem postSearchItem);

    SearchAfterResult<PostSearchItem, Long> search(SearchQueryRequest searchQueryRequest);

    void remove(Long id);
}
