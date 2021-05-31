package com.petfabula.domain.aggregate.community.entity;

import com.petfabula.domain.base.GeneralEntity;
import com.petfabula.domain.common.validation.EntityValidationUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "post_comment",
        indexes = {@Index(name = "post_id_index",  columnList="post_id")})
public class PostComment extends GeneralEntity {

    public PostComment(Long id, Participator participator, Long postId, String content) {
        EntityValidationUtils.validStringLendth("content", content, 1, 240);
        setId(id);
        this.content = content;
        this.participator = participator;
        this.postId = postId;
        this.replyCount = 0;
    }


    @Column(name = "post_id")
    private Long postId;

    @Column(name = "content")
    private String content;

    @Column(name = "reply_count")
    private Integer replyCount;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "participator_id")
    private Participator participator;

    public void setReplyCount(Integer replyCount) {
        this.replyCount = replyCount;
    }

}
