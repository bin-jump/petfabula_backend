package com.petfabula.application.event;

import com.petfabula.domain.aggregate.community.question.entity.Answer;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class AnswerCreateEvent implements Serializable {

    private Answer answer;
}
