package com.petfabula.domain.aggregate.community.participator;

import com.petfabula.domain.common.domain.ValueObject;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Getter
@Embeddable
@NoArgsConstructor
public class FollowParticipatorId extends ValueObject {

    public FollowParticipatorId(Long followerId, Long followedId) {
        this.followerId = followerId;
        this.followedId = followedId;
    }

    // participator should comes first for indexing
    @Column(name = "follower_id")
    private Long followerId;

    @Column(name = "followed_id")
    private Long followedId;

    @Override
    protected Object[] getCompareValues() {
        return new Object[]{this.followedId, this.followerId};
    }
}
