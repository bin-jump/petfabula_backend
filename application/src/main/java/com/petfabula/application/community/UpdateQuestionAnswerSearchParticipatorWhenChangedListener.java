package com.petfabula.application.community;

import com.petfabula.application.event.AccountUpdateEvent;
import com.petfabula.domain.aggregate.community.question.QuestionAnswerSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class UpdateQuestionAnswerSearchParticipatorWhenChangedListener {

    @Autowired
    private QuestionAnswerSearchService questionAnswerSearchService;

    @Async
    @TransactionalEventListener
    public void handle(AccountUpdateEvent event) {
        questionAnswerSearchService
                .updateParticipatorInfo(event.getId(), event.getPhoto());
    }
}
