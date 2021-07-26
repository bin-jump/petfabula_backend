package com.petfabula.domain.aggregate.community.question.entity.valueobject;

import com.petfabula.domain.aggregate.community.participator.entity.Participator;
import com.petfabula.domain.aggregate.community.question.entity.Answer;
import com.petfabula.domain.common.domain.EntityBase;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.Instant;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "answer_like", uniqueConstraints={
        @UniqueConstraint(columnNames = {"participator_id", "answer_id"})})
public class UpvoteAnswer extends EntityBase {

    public UpvoteAnswer(Long id, Participator participator, Answer answer) {
        setId(id);
        this.participatorId = participator.getId();
        this.answerId = answer.getId();
    }

    @Column(name = "participator_id")
    private Long participatorId;

    @Column(name = "answer_id")
    private Long answerId;

    @CreatedDate
    @Column(name = "created_date", nullable = false, updatable = false)
    private Instant createdDate = Instant.now();
}
