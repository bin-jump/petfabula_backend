package com.petfabula.domain.aggregate.community.common;

import com.petfabula.domain.aggregate.community.participator.entity.Participator;
import com.petfabula.domain.common.domain.GeneralEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter
@MappedSuperclass
public class CommentBase extends GeneralEntity {

    @Column(name = "content")
    private String content;

    @Column(name = "reply_count")
    private Integer replyCount;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "participator_id", foreignKey = @ForeignKey(name = "none"))
    private Participator participator;

    public void setReplyCount(Integer replyCount) {
        this.replyCount = replyCount;
    }
}
