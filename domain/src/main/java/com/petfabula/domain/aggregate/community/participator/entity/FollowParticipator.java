package com.petfabula.domain.aggregate.community.participator.entity;

import com.petfabula.domain.aggregate.community.participator.entity.Participator;
import com.petfabula.domain.common.domain.EntityBase;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.Instant;

@NamedEntityGraph(name = "follow.bare")
@Getter
@Entity
@NoArgsConstructor
@Table(name = "follow_participator", uniqueConstraints={
        @UniqueConstraint(columnNames = {"follower_id", "followed_id"})})
public class FollowParticipator extends EntityBase {

    public FollowParticipator(Long id, Participator follower, Participator followed) {
        setId(id);
        this.followerId = follower.getId();
        this.followedId = followed.getId();
    }

    @Column(name = "follower_id")
    private Long followerId;

    @Column(name = "followed_id")
    private Long followedId;

    @CreatedDate
    @Column(name = "created_date", nullable = false, updatable = false)
    private Instant createdDate = Instant.now();
}
