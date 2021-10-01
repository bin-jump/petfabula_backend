package com.petfabula.infrastructure.persistence.elasticsearch.community;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.petfabula.domain.aggregate.community.question.QuestionAnswerSearchItem;
import com.petfabula.domain.aggregate.community.question.QuestionAnswerSearchService;
import com.petfabula.domain.common.search.SearchAfterResult;
import com.petfabula.domain.common.search.SearchQueryRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.elasticsearch.index.reindex.UpdateByQueryAction;
import org.elasticsearch.index.reindex.UpdateByQueryRequest;
import org.elasticsearch.index.reindex.UpdateByQueryRequestBuilder;
import org.elasticsearch.script.Script;
import org.elasticsearch.script.ScriptType;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class QuestionAnswerSearchServiceImpl implements QuestionAnswerSearchService {

    static String INDICE_NAME = "question_answer";

    private ModelMapper modelMapper = new ModelMapper();

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private QuestionAnswerSearchRepository questionAnswerSearchRepository;

    @Autowired
    private RestHighLevelClient client;

    @Override
    public void index(QuestionAnswerSearchItem questionAnswerSearchItem) {
        questionAnswerSearchRepository
                .save(QuestionAnswerDocument.of(questionAnswerSearchItem));
    }

    @Override
    public SearchAfterResult<QuestionAnswerSearchItem, Long> search(SearchQueryRequest searchQueryRequest) {
        try {
            SearchRequest searchRequest = new SearchRequest(INDICE_NAME);
            SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
            sourceBuilder
                    .query(QueryBuilders.multiMatchQuery(searchQueryRequest.getQuery(),
                            "title", "content", "answerContent"
                            ));
            sourceBuilder
                    .size(searchQueryRequest.getPageSize())
//                    .sort("_score", SortOrder.DESC)
                    .sort("id", SortOrder.DESC);

            if (searchQueryRequest.hasCursor()) {
                sourceBuilder.searchAfter(new Object[]{searchQueryRequest.getCursor()});
            }
            searchRequest.source(sourceBuilder);
            SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);

            List<QuestionAnswerDocument> questionAnswerDocuments = new ArrayList<>();
            Arrays.stream(response.getHits().getHits()).forEach(
                    item -> {
                        try {
                            questionAnswerDocuments.add(mapper.readValue(item.getSourceAsString(), QuestionAnswerDocument.class));
                        } catch (JsonProcessingException e) {
                            throw new RuntimeException("json parse error " + e);
                        }
                    });
            List<QuestionAnswerSearchItem> res = questionAnswerDocuments.stream()
                    .map(item -> modelMapper.map(item, QuestionAnswerSearchItem.class))
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
    public void updateAnswerQuestionTitle(Long questionId, String title) {
        UpdateByQueryRequest request = new UpdateByQueryRequest(INDICE_NAME);
        TermQueryBuilder qidQuery = QueryBuilders.termQuery("questionId", questionId);
        request.setConflicts("proceed");
        request.setQuery(qidQuery);
        request.setScript(new Script(ScriptType.INLINE, "painless","ctx._source.title = '" + title + "'",
                Collections.emptyMap()));
        request.setRefresh(true);
        try {
            client.updateByQuery(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.error("update failed " + e);
            throw new RuntimeException("update failed");
        }
    }

    @Override
    public void updateParticipatorInfo(Long participatorId, String photo) {
        UpdateByQueryRequest request = new UpdateByQueryRequest(INDICE_NAME);

        QueryBuilder pidQuery = QueryBuilders.nestedQuery("participator",
                QueryBuilders.termQuery("participator.id", participatorId), ScoreMode.None);
        request.setConflicts("proceed");
        request.setQuery(pidQuery);
        request.setScript(new Script(ScriptType.INLINE, "painless","ctx._source.participator.photo = '"
                + photo + "'",
                Collections.emptyMap()));
        request.setRefresh(true);
        try {
            client.updateByQuery(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.error("update failed " + e);
            throw new RuntimeException("update failed");
        }
    }

    @Override
    public void removeByQuestionId(Long questionId) {
        DeleteByQueryRequest request = new DeleteByQueryRequest(INDICE_NAME);
        TermQueryBuilder qidQuery = QueryBuilders.termQuery("questionId", questionId);
        request.setConflicts("proceed");
        request.setQuery(qidQuery);
        request.setRefresh(true);
        try {
            client.deleteByQuery(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.error("remove failed " + e);
            throw new RuntimeException("remove failed");
        }
    }

    @Override
    public void removeAnswerByAnswerId(Long answerId) {
        DeleteByQueryRequest request = new DeleteByQueryRequest(INDICE_NAME);
        TermQueryBuilder aidQuery = QueryBuilders.termQuery("answerId", answerId);
        request.setConflicts("proceed");
        request.setQuery(aidQuery);
        request.setRefresh(true);
        try {
            client.deleteByQuery(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.error("remove failed " + e);
            throw new RuntimeException("remove failed");
        }
    }

    @Override
    public void removeQuestionByQuestionId(Long questionId) {
        DeleteByQueryRequest request = new DeleteByQueryRequest(INDICE_NAME);
        TermQueryBuilder qidQuery = QueryBuilders.termQuery("questionId", questionId);
        TermQueryBuilder typeQuery = QueryBuilders
                .termQuery("category", QuestionAnswerSearchItem.ItemType.QUESTION.toString());
        QueryBuilder qQuery = QueryBuilders
                .boolQuery()
                .must(qidQuery)
                .must(typeQuery);
        request.setConflicts("proceed");
        request.setQuery(qQuery);
        request.setRefresh(true);
        try {
            client.deleteByQuery(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.error("remove failed " + e);
            throw new RuntimeException("remove failed");
        }
    }
}
