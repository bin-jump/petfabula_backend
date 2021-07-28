package com.petfabula.domain.aggregate.community.question.entity;

import com.petfabula.domain.aggregate.community.common.CommentBase;
import com.petfabula.domain.aggregate.community.participator.entity.Participator;
import com.petfabula.domain.common.domain.GeneralEntity;
import com.petfabula.domain.common.validation.EntityValidationUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "answer_comment",
        indexes = {@Index(name = "answer_id_index",  columnList="answer_id")})
public class AnswerComment extends GeneralEntity {

    public AnswerComment(Long id, Participator participator,
                         Long answerId, Long questionId, Long replyTo, String content) {
        EntityValidationUtils.validStringLendth("content", content, 1, 240);
        setId(id);
        this.content = content;
        this.participator = participator;
        this.questionId = questionId;
        this.answerId = answerId;
        this.replyTo = replyTo;
    }

    @Column(name = "question_id", nullable = false)
    private Long questionId;

    @Column(name = "content")
    private String content;

    @Column(name = "reply_to")
    private Long replyTo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "participator_id", foreignKey = @ForeignKey(name = "none"))
    private Participator participator;

    @Column(name = "answer_id", nullable = false)
    private Long answerId;

}
