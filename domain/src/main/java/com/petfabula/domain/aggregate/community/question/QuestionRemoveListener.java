package com.petfabula.domain.aggregate.community.question;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class QuestionRemoveListener {

    @Autowired
    private QuestionAnswerSearchService searchService;

    @Async
    @TransactionalEventListener
    public void handle(QuestionRemoved questionRemoved) {
        // remove question and answers
        searchService.removeByQuestionId(questionRemoved.getQuestion().getId());
    }
}
