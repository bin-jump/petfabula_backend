package com.petfabula.application.event;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class QuestionLikedEvent implements Serializable {

    private Long questionId;

    private Long participatorId;
}
