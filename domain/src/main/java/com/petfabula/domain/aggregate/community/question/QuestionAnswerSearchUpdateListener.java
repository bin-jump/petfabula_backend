package com.petfabula.domain.aggregate.community.question;

import com.petfabula.domain.aggregate.community.question.entity.Question;
import com.petfabula.domain.aggregate.community.question.service.QuestionIdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class QuestionAnswerSearchUpdateListener {

    @Autowired
    private QuestionAnswerSearchService searchService;

    @Autowired
    private QuestionIdGenerator questionIdGenerator;

    @Async
    @TransactionalEventListener
    public void handle(QuestionAnswerUpdated questionAnswerUpdated) {
        QuestionAnswerSearchItem questionAnswerSearchItem;

        if (questionAnswerUpdated.getAnswer() == null) {
            //update question
            Question question = questionAnswerUpdated.getQuestion();
            searchService.updateAnswerQuestionTitle(question.getId(),
                    question.getTitle());

            questionAnswerSearchItem = QuestionAnswerSearchItem
                    .of(questionAnswerUpdated.getQuestion(), questionIdGenerator.nextId());
            searchService.removeQuestionByQuestionId(question.getId());
            searchService.index(questionAnswerSearchItem);
        } else {
            questionAnswerSearchItem = QuestionAnswerSearchItem
                    .of(questionAnswerUpdated.getQuestion(), questionAnswerUpdated.getAnswer(),
                            questionIdGenerator.nextId());

            searchService.removeAnswerByAnswerId(questionAnswerUpdated.getAnswer().getId());
            searchService.index(questionAnswerSearchItem);
        }
    }
}
