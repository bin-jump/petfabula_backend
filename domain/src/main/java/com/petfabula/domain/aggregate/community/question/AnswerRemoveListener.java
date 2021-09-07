package com.petfabula.domain.aggregate.community.question;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class AnswerRemoveListener {

    @Autowired
    private QuestionAnswerSearchService searchService;

    @Async
    @TransactionalEventListener
    public void handle(AnswerRemoved answerRemoved) {
        searchService.removeAnswerByAnswerId(answerRemoved.getAnswer().getId());
    }
}
