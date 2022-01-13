package com.petfabula.presentation.web.controller;

import com.petfabula.domain.aggregate.community.post.PostSearchItem;
import com.petfabula.domain.aggregate.community.post.PostSearchService;
import com.petfabula.domain.aggregate.community.question.QuestionAnswerSearchItem;
import com.petfabula.domain.aggregate.community.question.QuestionAnswerSearchService;
import com.petfabula.domain.common.search.SearchAfterResult;
import com.petfabula.domain.common.search.SearchQueryRequest;
import com.petfabula.presentation.facade.assembler.community.PostAssembler;
import com.petfabula.presentation.facade.assembler.community.QuestionAssembler;
import com.petfabula.presentation.facade.dto.community.PostDto;
import com.petfabula.presentation.facade.dto.community.QuestionAnswerSearchDto;
import com.petfabula.presentation.web.api.CursorPageData;
import com.petfabula.presentation.web.api.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/search")
@Validated
public class SearchController {

    static final int DEAULT_PAGE_SIZE = 10;

    @Autowired
    private PostAssembler postAssembler;

    @Autowired
    private QuestionAssembler questionAssembler;

    @Autowired
    private PostSearchService postSearchService;

    @Autowired
    private QuestionAnswerSearchService questionAnswerSearchService;

    @GetMapping("post")
    public Response<CursorPageData<PostDto>> searchPost(@RequestParam(value = "cursor", required = false) Long after,
                                                    @RequestParam(value = "q") String q) {

        SearchQueryRequest queryRequest = new SearchQueryRequest(q, DEAULT_PAGE_SIZE, after);
        SearchAfterResult<PostSearchItem, Long> searchRes = postSearchService.search(queryRequest);
        CursorPageData<PostDto> res = CursorPageData
                .of(postAssembler.convertSearchDtos(searchRes.getResult()), searchRes.isHasMore(),
                        searchRes.getPageSize(), searchRes.getNextCursor());
        return Response.ok(res);
    }

    @GetMapping("question")
    public Response<CursorPageData<QuestionAnswerSearchDto>> searchQuestion(@RequestParam(value = "cursor", required = false) Long after,
                                                                            @RequestParam(value = "q") String q) {

        SearchQueryRequest queryRequest = new SearchQueryRequest(q, DEAULT_PAGE_SIZE, after);
        SearchAfterResult<QuestionAnswerSearchItem, Long> searchRes = questionAnswerSearchService
                .search(queryRequest);

        CursorPageData<QuestionAnswerSearchDto> res = CursorPageData
                .of(questionAssembler.convertSearchDtos(searchRes.getResult()), searchRes.isHasMore(),
                        searchRes.getPageSize(), searchRes.getNextCursor());
        return Response.ok(res);
    }
}
