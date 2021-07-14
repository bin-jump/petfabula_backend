package com.petfabula.domain.aggregate.community.post.entity;

import com.petfabula.domain.aggregate.community.participator.entity.Participator;
import com.petfabula.domain.common.domain.GeneralEntity;
import com.petfabula.domain.common.validation.EntityValidationUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NamedEntityGraph(name = "postComment.all",
        attributeNodes = {@NamedAttributeNode("participator")}
)
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
    @JoinColumn(name = "participator_id", foreignKey = @ForeignKey(name = "none"))
    private Participator participator;

    public void setReplyCount(Integer replyCount) {
        this.replyCount = replyCount;
    }

}
