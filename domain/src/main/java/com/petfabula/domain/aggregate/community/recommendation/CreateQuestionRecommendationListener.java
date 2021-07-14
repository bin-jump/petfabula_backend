package com.petfabula.domain.aggregate.community.recommendation;

import com.petfabula.domain.aggregate.community.question.QuestionCreated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class CreateQuestionRecommendationListener {

    @Autowired
    private QuestionRecommendationService questionRecommendationService;

    @Async
    @TransactionalEventListener
    public void handle(QuestionCreated questionCreated) {
        questionRecommendationService.addNewQuestion(questionCreated.getQuestion());
    }
}
