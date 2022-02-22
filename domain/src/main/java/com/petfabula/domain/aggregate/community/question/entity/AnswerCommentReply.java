package com.petfabula.domain.aggregate.community.question.entity;

import com.petfabula.domain.aggregate.community.common.CommentReplyBase;
import com.petfabula.domain.aggregate.community.participator.entity.Participator;
import com.petfabula.domain.common.util.ValueUtil;
import com.petfabula.domain.common.validation.EntityValidationUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

//@Getter
//@Entity
//@NoArgsConstructor
//@Table(name = "answer_comment_reply",
//        indexes = {@Index(name = "comment_id_index",  columnList="comment_id")})
public class AnswerCommentReply extends CommentReplyBase {

    public AnswerCommentReply(Long id, Participator participator, Long answerId,
                              Long answerCommentId, Long replyToId, String content) {
        EntityValidationUtils.validStringLength("content", content, 1, 240);
        setId(id);
        content = ValueUtil.trimContent(content);
        this.content = content;
        this.participator = participator;
        this.answerId = answerId;
        this.commentId = answerCommentId;
        this.replyToId = replyToId;
    }

    @Column(name = "answer_id")
    private Long answerId;
}
