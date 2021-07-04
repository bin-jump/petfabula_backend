package com.petfabula.domain.aggregate.community.common;

import com.petfabula.domain.aggregate.community.participator.entity.Participator;
import com.petfabula.domain.common.domain.GeneralEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@MappedSuperclass
public abstract class CommentReplyBase extends GeneralEntity {

    @Column(name = "comment_id", nullable = false)
    protected Long commentId;

    @Column(name = "reply_to_id")
    protected Long replyToId;

    @Column(name = "content", nullable = false)
    protected String content;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "participator_id", foreignKey = @ForeignKey(name = "none"))
    protected Participator participator;
}
