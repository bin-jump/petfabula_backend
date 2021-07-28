package com.petfabula.application.event;

import com.petfabula.domain.aggregate.community.question.entity.AnswerComment;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AnswerCommentCreateEvent {

    private AnswerComment answerComment;
}
