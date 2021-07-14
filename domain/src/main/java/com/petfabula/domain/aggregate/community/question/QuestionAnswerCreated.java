package com.petfabula.domain.aggregate.community.question;

import com.petfabula.domain.aggregate.community.participator.ParticipatorSearchItem;
import com.petfabula.domain.aggregate.community.question.entity.Answer;
import com.petfabula.domain.aggregate.community.question.entity.Question;
import com.petfabula.domain.aggregate.community.question.entity.QuestionImage;
import com.petfabula.domain.common.domain.DomainEvent;
import com.petfabula.domain.common.search.SearchImageItem;
import lombok.Getter;

@Getter
public class QuestionAnswerCreated extends DomainEvent {

    private Question question;

    private Answer answer;

    public QuestionAnswerCreated(Question question) {
        this.question = question;
    }

    public QuestionAnswerCreated(Answer answer, Question question) {
        this.question = question;
        this.answer = answer;
    }

}
