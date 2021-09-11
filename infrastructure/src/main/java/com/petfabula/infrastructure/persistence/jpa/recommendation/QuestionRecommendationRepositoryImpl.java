package com.petfabula.infrastructure.persistence.jpa.recommendation;

import com.petfabula.domain.aggregate.community.question.entity.Question;
import com.petfabula.domain.aggregate.community.question.repository.QuestionRepository;
import com.petfabula.domain.aggregate.community.recommendation.QuestionRecommendation;
import com.petfabula.domain.aggregate.community.recommendation.QuestionRecommendationRepository;
import com.petfabula.domain.aggregate.community.recommendation.RecommendationResult;
import com.petfabula.domain.common.paging.OffsetPage;
import com.petfabula.infrastructure.persistence.jpa.community.question.repository.QuestionJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigInteger;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.springframework.data.util.Pair.toMap;

@Repository
public class QuestionRecommendationRepositoryImpl implements QuestionRecommendationRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private QuestionRecommendationJpaRepository questionRecommendationJpaRepository;

    @Autowired
    private QuestionRepository questionRepository;


    @Override
    public QuestionRecommendation save(QuestionRecommendation questionRecommendation) {
        return questionRecommendationJpaRepository.save(questionRecommendation);
    }

    @Override
    public RecommendationResult<Question> findRandomRecommend(int page, int size, int seed, Long cursor) {
        // cursor here is for filtering new created questions
        if(cursor == null) {
            QuestionRecommendation questionRecommendation =
                    questionRecommendationJpaRepository.findTopByOrderByIdDesc();
            if (questionRecommendation == null) {
                return new RecommendationResult<>(OffsetPage.ofEmpty(page, size), seed, cursor);
            }
            cursor =  questionRecommendation.getId();
        }

//        String q = "select q from QuestionRecommendation q where q.id < :cursor order by rand(42)";

        String q = "select question_id from question_recommendation where (:cursor is null or id <= :cursor) order by rand(:seed)";
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

        String countQ = "select count(*) from question_recommendation where (:cursor is null or id <= :cursor) order by rand(:seed)";
        Query countQuery = entityManager.createNativeQuery(countQ);
        countQuery.setParameter("cursor", cursor);
        countQuery.setParameter("seed", seed);
        BigInteger cnt = (BigInteger)countQuery.getSingleResult();
        Long count = cnt.longValue();
        boolean hasMore = (count - page * size) > 0;

        Map<Long, Question> questionMap = questionRepository
                .findByIds(questionIds)
                .stream().collect(Collectors.toMap(Question::getId,
                        Function.identity()));
        List<Question> questions = questionIds.stream()
                .map(item -> questionMap.get(item))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        OffsetPage<Question> offsetPage =
        OffsetPage.of(questions, page, size, questions.size() == size);

        return new RecommendationResult<>(offsetPage, seed, cursor);
    }
}
