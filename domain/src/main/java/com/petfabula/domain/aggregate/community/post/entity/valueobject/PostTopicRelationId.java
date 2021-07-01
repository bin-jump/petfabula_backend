package com.petfabula.domain.aggregate.community.post.entity.valueobject;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@EqualsAndHashCode
@Getter
@Embeddable
@NoArgsConstructor
public class PostTopicRelationId implements Serializable {

    public PostTopicRelationId(Long postId, Long postTopicId) {
        this.postTopicId = postTopicId;
        this.postId = postId;
    }

    @Column(name = "post_id")
    private Long postId;

    @Column(name = "post_topic_id")
    private Long postTopicId;

}
