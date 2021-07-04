package com.petfabula.domain.aggregate.community.question.entity.valueobject;

import com.petfabula.domain.aggregate.community.participator.entity.Participator;
import com.petfabula.domain.aggregate.community.question.entity.Question;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.Instant;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "question_like")
public class UpvoteQuestion {

    public UpvoteQuestion(Participator participator, Question question) {
        this.upvoteQuestionId = new UpvoteQuestionId(participator.getId(), question.getId());
        this.participator = participator;
        this.question = question;
    }

    @EmbeddedId
    private UpvoteQuestionId upvoteQuestionId;

    @ManyToOne()
    @JoinColumn(foreignKey = @ForeignKey(name = "none"))
    @MapsId("participatorId")
    private Participator participator;

    @ManyToOne()
    @JoinColumn(foreignKey = @ForeignKey(name = "none"))
    @MapsId("questionId")
    private Question question;

    @CreatedDate
    @Column(name = "created_date", nullable = false, updatable = false)
    private Instant createdDate = Instant.now();
}
