package com.petfabula.domain.aggregate.community.entity;

import com.petfabula.domain.aggregate.community.entity.valueobject.FollowParticipatorId;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "follow_participator")
public class FollowParticipator {

    public FollowParticipator(Participator follower, Participator followed) {
        this.followParticipatorId = new FollowParticipatorId(follower.getId(), followed.getId());
        this.follower = follower;
        this.followed = followed;
    }

    @EmbeddedId
    private FollowParticipatorId followParticipatorId;

    @ManyToOne()
    @MapsId("followerId")
    private Participator follower;

    @ManyToOne()
    @MapsId("followedId")
    private Participator followed;
}
