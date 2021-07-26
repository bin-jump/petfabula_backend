package com.petfabula.domain.aggregate.community.question.entity.valueobject;

import com.petfabula.domain.aggregate.community.participator.entity.Participator;
import com.petfabula.domain.aggregate.community.question.entity.Question;
import com.petfabula.domain.common.domain.EntityBase;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.Instant;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "question_like", uniqueConstraints={
        @UniqueConstraint(columnNames = {"participator_id", "question_id"})})
public class UpvoteQuestion extends EntityBase {

    public UpvoteQuestion(Long id, Participator participator, Question question) {
        setId(id);
        this.participatorId = participator.getId();
        this.questionId = question.getId();
    }

    @Column(name = "participator_id")
    private Long participatorId;

    @Column(name = "question_id")
    private Long questionId;

    @CreatedDate
    @Column(name = "created_date", nullable = false, updatable = false)
    private Instant createdDate = Instant.now();
}
