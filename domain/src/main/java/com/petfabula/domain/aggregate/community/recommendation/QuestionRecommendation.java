package com.petfabula.domain.aggregate.community.recommendation;

import com.petfabula.domain.aggregate.community.participator.entity.Participator;
import com.petfabula.domain.aggregate.community.question.entity.Question;
import com.petfabula.domain.common.domain.GeneralEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NamedEntityGraph(name = "questionRecommendation.bare")
@Getter
@Entity
@NoArgsConstructor
@Table(name = "question_recommendation")
public class QuestionRecommendation extends GeneralEntity {

    public QuestionRecommendation(Long id, Long questionId) {
        setId(id);
        this.questionId = questionId;
    }

//    @OneToOne(fetch = FetchType.EAGER)
//    @JoinColumn(name = "question_id", foreignKey = @ForeignKey(name = "none"))
//    private Question question;

    @Column(name = "question_id")
    private Long questionId;
}
