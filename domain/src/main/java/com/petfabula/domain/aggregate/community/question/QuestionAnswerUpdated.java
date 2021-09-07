package com.petfabula.domain.aggregate.community.question;

import com.petfabula.domain.aggregate.community.question.entity.Answer;
import com.petfabula.domain.aggregate.community.question.entity.Question;
import com.petfabula.domain.common.domain.DomainEvent;
import lombok.Getter;

@Getter
public class QuestionAnswerUpdated extends DomainEvent {

    private Question question;

    private Answer answer;

    public QuestionAnswerUpdated(Question question) {
        this.question = question;
    }

    public QuestionAnswerUpdated(Answer answer, Question question) {
        this.question = question;
        this.answer = answer;
    }
}
