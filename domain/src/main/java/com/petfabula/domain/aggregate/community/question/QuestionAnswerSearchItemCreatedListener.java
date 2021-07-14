package com.petfabula.domain.aggregate.community.question;

import com.petfabula.domain.aggregate.community.question.service.QuestionIdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class QuestionAnswerSearchItemCreatedListener {

    @Autowired
    private QuestionAnswerSearchService searchService;

    @Autowired
    private QuestionIdGenerator questionIdGenerator;

    @Async
    @TransactionalEventListener
    public void handle(QuestionAnswerCreated questionAnswerCreated) {
        QuestionAnswerSearchItem questionAnswerSearchItem;
        if (questionAnswerCreated.getAnswer() == null) {
            questionAnswerSearchItem = QuestionAnswerSearchItem
                    .of(questionAnswerCreated.getQuestion(), questionIdGenerator.nextId());
        } else {
            questionAnswerSearchItem = QuestionAnswerSearchItem
                    .of(questionAnswerCreated.getQuestion(), questionAnswerCreated.getAnswer(),
                            questionIdGenerator.nextId());
        }
        searchService.index(questionAnswerSearchItem);
    }

}
