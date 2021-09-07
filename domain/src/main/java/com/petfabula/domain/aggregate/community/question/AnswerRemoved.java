package com.petfabula.domain.aggregate.community.question;

import com.petfabula.domain.aggregate.community.question.entity.Answer;
import com.petfabula.domain.common.domain.DomainEvent;
import lombok.Getter;

@Getter
public class AnswerRemoved extends DomainEvent {

    private Answer answer;

    public AnswerRemoved(Answer answer) {
        this.answer = answer;
    }
}
