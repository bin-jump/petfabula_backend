package com.petfabula.infrastructure.persistence.elasticsearch.community;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.petfabula.domain.aggregate.community.post.PostSearchItem;
import com.petfabula.domain.aggregate.community.post.PostSearchService;
import com.petfabula.domain.common.search.SearchAfterResult;
import com.petfabula.domain.common.search.SearchQueryRequest;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class PostSearchServiceImpl implements PostSearchService {

    private ModelMapper modelMapper = new ModelMapper();

    @Autowired
    private ObjectMapper mapper;


    @Autowired
    private PostSearchRepository postSearchRepository;

    @Autowired
    private RestHighLevelClient client;

    @Override
    public void index(PostSearchItem postSearchItem) {
        postSearchRepository.save(PostDocument.of(postSearchItem));
    }

    @Override
    public SearchAfterResult<PostSearchItem, Long> search(SearchQueryRequest searchQueryRequest) {

        try {
            SearchRequest searchRequest = new SearchRequest("post");
            SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
            sourceBuilder
                    .query(QueryBuilders.matchQuery("content",
                            searchQueryRequest.getQuery()));
            sourceBuilder
                    .size(searchQueryRequest.getPageSize())
//                    .sort("_score", SortOrder.DESC)
                    .sort("id", SortOrder.DESC);

            if (searchQueryRequest.hasCursor()) {
                sourceBuilder.searchAfter(new Object[]{searchQueryRequest.getCursor()});
            }
            searchRequest.source(sourceBuilder);
            SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);

            List<PostDocument> postDocuments = new ArrayList<>();
            Arrays.stream(response.getHits().getHits()).forEach(
                item -> {
                    try {
                      postDocuments.add(mapper.readValue(item.getSourceAsString(), PostDocument.class));
                    } catch (JsonProcessingException  e) {
                        throw new RuntimeException("json parse error " + e);
                    }
                });
            List<PostSearchItem> res = postDocuments.stream()
                    .map(item -> modelMapper.map(item, PostSearchItem.class))
                    .collect(Collectors.toList());

            Long cursor = null;
            if (res.size() > 0) {
                cursor = res.get(res.size() - 1).getId();
            }
            return new SearchAfterResult<>(cursor, res,
                    res.size() == searchQueryRequest.getPageSize(), searchQueryRequest.getPageSize());
        } catch (IOException e) {
            log.error("Search failed " + e);
            throw new RuntimeException("Search failed");
        }
    }

    @Override
    public void remove(Long id) {
        postSearchRepository.deleteById(id);
    }
}
