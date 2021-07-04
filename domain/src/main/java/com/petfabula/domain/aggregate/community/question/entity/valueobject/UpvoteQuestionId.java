package com.petfabula.domain.aggregate.community.question.entity.valueobject;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@EqualsAndHashCode
@Getter
@Embeddable
@NoArgsConstructor
public class UpvoteQuestionId implements Serializable {

    public UpvoteQuestionId(Long participatorId, Long questionId) {
        this.participatorId = participatorId;
        this.questionId = questionId;
    }

    @Column(name = "participator_id")
    private Long participatorId;

    @Column(name = "question_id")
    private Long questionId;
}
