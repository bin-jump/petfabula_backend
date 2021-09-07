package com.petfabula.domain.aggregate.community.question;

import com.petfabula.domain.aggregate.community.question.entity.Question;
import com.petfabula.domain.common.domain.DomainEvent;
import lombok.Getter;

@Getter
public class QuestionRemoved extends DomainEvent {

    private Question question;

    public QuestionRemoved(Question question) {
        this.question = question;
    }
}
