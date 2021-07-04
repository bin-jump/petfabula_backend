package com.petfabula.domain.aggregate.community.question.entity;

import com.petfabula.domain.common.image.ImageEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "answer_image",
        indexes = {@Index(name = "answer_id_index",  columnList="answer_id")})
public class AnswerImage extends ImageEntity {

    public AnswerImage(Long id, String uri, Long answerId, Integer w, Integer h) {
        setId(id);
        this.url = uri;
        this.width = w;
        this.height = h;
        this.answerId = answerId;
    }

    @Column(name = "answer_id")
    private Long answerId;
}