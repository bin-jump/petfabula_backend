package com.petfabula.presentation.web.controller;

import com.petfabula.domain.aggregate.community.post.entity.Post;
import com.petfabula.domain.aggregate.community.question.entity.Question;
import com.petfabula.domain.aggregate.community.recommendation.PostRecommendationService;
import com.petfabula.domain.aggregate.community.recommendation.QuestionRecommendation;
import com.petfabula.domain.aggregate.community.recommendation.QuestionRecommendationService;
import com.petfabula.domain.aggregate.community.recommendation.RecommendationResult;
import com.petfabula.domain.common.paging.CursorPage;
import com.petfabula.domain.exception.InvalidOperationException;
import com.petfabula.presentation.facade.assembler.community.PostAssembler;
import com.petfabula.presentation.facade.assembler.community.QuestionAssembler;
import com.petfabula.presentation.facade.dto.community.PostDto;
import com.petfabula.presentation.facade.dto.community.QuestionDto;
import com.petfabula.presentation.web.api.CursorPageData;
import com.petfabula.presentation.web.api.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/recommend")
@Validated
public class RecommendationController {

    static final int DEAULT_PAGE_SIZE = 5;

    @Autowired
    private QuestionAssembler questionAssembler;

    @Autowired
    private PostAssembler postAssembler;

    @Autowired
    private QuestionRecommendationService questionRecommendationService;

    @Autowired
    private PostRecommendationService postRecommendationService;

    @GetMapping("questions")
    public Response<CursorPageData<QuestionDto>> getRecommandQuestions(@RequestParam(value = "cursor", required = false) String cursor) {
        RecommendationResult<Question> recommendationResult = null;
        if (cursor == null) {
            recommendationResult =
                    questionRecommendationService
                            .randomRecommendation(1, DEAULT_PAGE_SIZE, null, null);
        } else {
            Object[] parts = toRecommendPageInfo(cursor);
            recommendationResult =
                    questionRecommendationService
                            .randomRecommendation((Integer)parts[0], DEAULT_PAGE_SIZE, (Integer)parts[1], (Long)parts[2]);
        }
        cursor = toCursor(recommendationResult.getResult().getPageIndex() + 1, recommendationResult.getSeed(),
                recommendationResult.getCursor());

        List<Question> questions = recommendationResult.getResult().getResult();

        CursorPageData<QuestionDto> res = CursorPageData
                .of(questionAssembler.convertToDtos(questions), recommendationResult.getResult().isHasMore(),
                        recommendationResult.getResult().getPageSize(), cursor);

        return Response.ok(res);
    }

    @GetMapping("posts")
    public Response<CursorPageData<PostDto>> getRecommandPosts(@RequestParam(value = "cursor", required = false) String cursor) {
        RecommendationResult<Post> recommendationResult = null;
        if (cursor == null) {
            recommendationResult =
                    postRecommendationService
                            .randomRecommendation(1, DEAULT_PAGE_SIZE, null, null);
        } else {
            Object[] parts = toRecommendPageInfo(cursor);
            recommendationResult =
                    postRecommendationService
                            .randomRecommendation((Integer)parts[0], DEAULT_PAGE_SIZE, (Integer)parts[1], (Long)parts[2]);
        }
        cursor = toCursor(recommendationResult.getResult().getPageIndex() + 1, recommendationResult.getSeed(),
                recommendationResult.getCursor());

        List<Post> posts = recommendationResult.getResult().getResult();

        CursorPageData<PostDto> res = CursorPageData
                .of(postAssembler.convertToDtos(posts), recommendationResult.getResult().isHasMore(),
                        recommendationResult.getResult().getPageSize(), cursor);

        return Response.ok(res);
    }

    private String toCursor(int page, int seed, Long cursor) {
        return page + "_" + seed + "_" + cursor;
    }

    private Object[] toRecommendPageInfo(String info) {
        try {
            String[] parts = info.split("_");
            if (parts.length != 3) {
                throw new InvalidOperationException("invalid value");
            }
            Integer page = Integer.parseInt(parts[0]);
            Integer seed = Integer.parseInt(parts[1]);
            Long cursor = Long.parseLong(parts[2]);
            return new Object[]{page, seed, cursor};
        } catch (NumberFormatException e) {
            throw new InvalidOperationException("invalid value");
        }
    }
}
