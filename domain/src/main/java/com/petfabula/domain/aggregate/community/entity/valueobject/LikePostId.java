package com.petfabula.domain.aggregate.community.entity.valueobject;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Getter
@Embeddable
public class LikePostId implements Serializable {

    public LikePostId(Long participatorId, Long postId) {
        this.participatorId = participatorId;
        this.postId = postId;
    }

    // participator should comes first for indexing
    @Column(name = "participator_id")
    private Long participatorId;

    @Column(name = "post_id")
    private Long postId;
}
