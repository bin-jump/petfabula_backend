package com.petfabula.domain.aggregate.community.post;

import com.petfabula.domain.common.search.SearchAfterResult;
import com.petfabula.domain.common.search.SearchRequest;

public interface SearchService {

    void indexPost(PostSearchItem postSearchItem);

    SearchAfterResult<Long, PostSearchItem> search(SearchRequest searchRequest);

    void updateContent(Long id, String content);

    void updateCounts(Long id, Integer likeCount, Integer commentCount, Integer viewCount);

    void remove(Long id);
}
