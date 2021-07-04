package com.petfabula.domain.aggregate.community.question.entity.valueobject;

import com.petfabula.domain.aggregate.community.participator.entity.Participator;
import com.petfabula.domain.aggregate.community.question.entity.Answer;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.Instant;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "answer_like")
public class UpvoteAnswer {

    public UpvoteAnswer(Participator participator, Answer answer) {
        this.upvoteAnswerId = new UpvoteAnswerId(participator.getId(), answer.getId());
        this.participator = participator;
        this.answer = answer;
    }

    @EmbeddedId
    private UpvoteAnswerId upvoteAnswerId;

    @ManyToOne()
    @JoinColumn(foreignKey = @ForeignKey(name = "none"))
    @MapsId("participatorId")
    private Participator participator;

    @ManyToOne()
    @JoinColumn(foreignKey = @ForeignKey(name = "none"))
    @MapsId("answerId")
    private Answer answer;

    @CreatedDate
    @Column(name = "created_date", nullable = false, updatable = false)
    private Instant createdDate = Instant.now();
}
