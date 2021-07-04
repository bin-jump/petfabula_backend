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
public class UpvoteAnswerId implements Serializable {

    public UpvoteAnswerId(Long participatorId, Long answerId) {
        this.participatorId = participatorId;
        this.answerId = answerId;
    }

    @Column(name = "participator_id")
    private Long participatorId;

    @Column(name = "answer_id")
    private Long answerId;
}
