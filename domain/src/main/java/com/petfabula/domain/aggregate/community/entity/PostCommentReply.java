package com.petfabula.domain.aggregate.community.entity;

import com.petfabula.domain.base.GeneralEntity;
import com.petfabula.domain.common.validation.EntityValidationUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "post_comment_reply",
        indexes = {@Index(name = "post_comment_id_index",  columnList="post_comment_id")})
public class PostCommentReply extends GeneralEntity {

    public PostCommentReply(Long id, Participator participator, Long postId, Long postCommentId, String content) {
        EntityValidationUtils.validStringLendth("content", content, 1, 240);
        setId(id);
        this.content = content;
        this.participator = participator;
        this.postId = postId;
        this.postCommentId = postCommentId;
    }

    @Column(name = "post_id")
    private Long postId;

    @Column(name = "post_comment_id")
    private Long postCommentId;

    @Column(name = "content")
    private String content;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "participator_id")
    private Participator participator;
}
