package com.petfabula.domain.aggregate.community.post.entity;

import com.petfabula.domain.aggregate.community.participator.entity.Participator;
import com.petfabula.domain.common.domain.GeneralEntity;
import com.petfabula.domain.common.validation.EntityValidationUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NamedEntityGraph(name = "postCommentReply.all",
        attributeNodes = {@NamedAttributeNode("participator")}
)
@Getter
@Entity
@NoArgsConstructor
@Table(name = "post_comment_reply",
        indexes = {@Index(name = "post_comment_id_index",  columnList="comment_id")})
public class PostCommentReply extends GeneralEntity {

    public PostCommentReply(Long id, Participator participator, Long postId,
                            Long postCommentId, Long replyToId, String content) {
        EntityValidationUtils.validStringLendth("content", content, 1, 240);
        setId(id);
        this.content = content;
        this.participator = participator;
        this.postId = postId;
        this.commentId = postCommentId;
        this.replyToId = replyToId;
    }

    @Column(name = "post_id", nullable = false)
    private Long postId;

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
