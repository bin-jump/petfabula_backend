package com.petfabula.domain.aggregate.community.question.entity;

import com.petfabula.domain.common.image.ImageEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "question_image",
        indexes = {@Index(name = "question_id_index",  columnList="question_id")})
public class QuestionImage extends ImageEntity {

    public QuestionImage(Long id, String uri, Long questionId, Integer w, Integer h) {
        setId(id);
        this.url = uri;
        this.width = w;
        this.height = h;
        this.questionId = questionId;
    }

    @Column(name = "question_id")
    private Long questionId;
}
