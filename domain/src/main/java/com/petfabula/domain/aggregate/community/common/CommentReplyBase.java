package com.petfabula.domain.aggregate.community.common;

import com.petfabula.domain.aggregate.community.participator.entity.Participator;
import com.petfabula.domain.common.domain.GeneralEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter
@MappedSuperclass
public class CommentReplyBase extends GeneralEntity {

    @Column(name = "comment_id", nullable = false)
    private Long commentId;

    @Column(name = "reply_to_id")
    private Long replyToId;

    @Column(name = "content", nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "participator_id", foreignKey = @ForeignKey(name = "none"))
    private Participator participator;
}
