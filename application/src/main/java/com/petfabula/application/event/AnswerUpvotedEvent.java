package com.petfabula.application.event;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AnswerUpvotedEvent {

    private Long answerId;

    private Long participatorId;
}
