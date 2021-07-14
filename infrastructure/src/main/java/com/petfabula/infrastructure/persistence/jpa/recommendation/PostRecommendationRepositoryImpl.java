package com.petfabula.infrastructure.persistence.jpa.recommendation;

import com.petfabula.domain.aggregate.community.post.entity.Post;
import com.petfabula.domain.aggregate.community.recommendation.PostRecommendation;
import com.petfabula.domain.aggregate.community.recommendation.PostRecommendationRepository;
import com.petfabula.domain.aggregate.community.recommendation.RecommendationResult;
import com.petfabula.domain.common.paging.OffsetPage;
import com.petfabula.infrastructure.persistence.jpa.community.post.repository.PostJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Repository
public class PostRecommendationRepositoryImpl implements PostRecommendationRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private PostRecommendationJpaRepository postRecommendationJpaRepository;

    @Autowired
    private PostJpaRepository postJpaRepository;

    @Override
    public PostRecommendation save(PostRecommendation postRecommendation) {
        return postRecommendationJpaRepository.save(postRecommendation);
    }

    @Override
    public RecommendationResult<Post> findRandomRecommend(int page, int size, int seed, Long cursor) {

        String q = "select post_id from post_recommendation where (:cursor is null or id <= :cursor) order by rand(:seed)";
        Query selectQuery = entityManager.createNativeQuery(q);
        selectQuery.setParameter("cursor", cursor);
        selectQuery.setParameter("seed", seed);

        selectQuery.setFirstResult((page - 1) * size);
        selectQuery.setMaxResults(size);

//        List<QuestionRecommendation> questionRecommendations = selectQuery.getResultList();
        List<Long> questionIds = ((List<BigInteger>)selectQuery.getResultList())
                .stream().map(item -> item.longValue()).collect(Collectors.toList());

        if (questionIds.size() == 0) {
            return new RecommendationResult<>(OffsetPage.ofEmpty(page, size), seed, cursor);
        }

        if(cursor == null) {
            PostRecommendation postRecommendation = postRecommendationJpaRepository
                    .findTopByOrderByIdDesc();
            cursor = postRecommendation.getId();
        }

        Map<Long, Post> questionMap = postJpaRepository
                .findByIdIn(questionIds)
                .stream().collect(Collectors.toMap(Post::getId,
                        Function.identity()));
        List<Post> questions = questionIds.stream()
                .map(item -> questionMap.get(item)).collect(Collectors.toList());

        OffsetPage<Post> offsetPage =
                OffsetPage.of(questions, page, size, questions.size() == size);

        return new RecommendationResult<>(offsetPage, seed, cursor);
    }
}
