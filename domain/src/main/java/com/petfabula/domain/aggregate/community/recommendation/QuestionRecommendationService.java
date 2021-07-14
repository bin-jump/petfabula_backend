package com.petfabula.domain.aggregate.community.recommendation;

import com.petfabula.domain.aggregate.community.question.entity.Question;
import com.petfabula.domain.common.paging.OffsetPage;
import com.petfabula.domain.exception.InvalidOperationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class QuestionRecommendationService {

    private Random random = new Random();

    @Autowired
    private QuestionRecommendationRepository questionRecommendationRepository;

    @Autowired
    private RecommendationIdGenerator recommendationIdGenerator;

    public RecommendationResult<Question> randomRecommendation(int page, int size, Integer seed, Long cursor) {
        if (page > 1 && (seed == null || cursor == null) || page < 1) {
            throw new InvalidOperationException("");
        }
        if (page == 1) {
            seed = random.nextInt();
        }

        RecommendationResult<Question> recommendations = questionRecommendationRepository
                .findRandomRecommend(page, size, seed, cursor);

        return recommendations;
    }

    public void addNewQuestion(Question question) {
        Long id = recommendationIdGenerator.nextId();
        QuestionRecommendation questionRecommendation = new QuestionRecommendation(id, question.getId());
        questionRecommendationRepository.save(questionRecommendation);
    }
}
