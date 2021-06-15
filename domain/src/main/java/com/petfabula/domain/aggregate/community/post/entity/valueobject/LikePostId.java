package com.petfabula.domain.aggregate.community.post.entity.valueobject;

import com.petfabula.domain.common.domain.ValueObject;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Getter
@Embeddable
@NoArgsConstructor
public class LikePostId extends ValueObject {

    public LikePostId(Long participatorId, Long postId) {
        this.participatorId = participatorId;
        this.postId = postId;
    }

    // participator should comes first for indexing
    @Column(name = "participator_id")
    private Long participatorId;

    @Column(name = "post_id")
    private Long postId;

    @Override
    protected Object[] getCompareValues() {
        return new Object[]{this.participatorId, this.postId};
    }
}
