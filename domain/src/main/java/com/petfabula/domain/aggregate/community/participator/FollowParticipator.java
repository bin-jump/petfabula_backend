package com.petfabula.domain.aggregate.community.participator;

import com.petfabula.domain.aggregate.community.participator.entity.Participator;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.Instant;

@NamedEntityGraph(name = "follow.bare")
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
    @JoinColumn(foreignKey = @ForeignKey(name = "none"))
    @MapsId("followerId")
    private Participator follower;

    @ManyToOne()
    @JoinColumn(foreignKey = @ForeignKey(name = "none"))
    @MapsId("followedId")
    private Participator followed;

    @CreatedDate
    @Column(name = "created_date", nullable = false, updatable = false)
    private Instant createdDate = Instant.now();
}
