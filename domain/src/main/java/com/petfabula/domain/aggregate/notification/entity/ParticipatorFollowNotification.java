package com.petfabula.domain.aggregate.notification.entity;

import com.petfabula.domain.common.domain.EntityBase;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.time.Instant;


@Getter
@Entity
@NoArgsConstructor
@Table(name = "notification_participator_follow", uniqueConstraints={
        @UniqueConstraint(columnNames = {"followee_id", "follower_id"})})
public class ParticipatorFollowNotification extends EntityBase {

    public ParticipatorFollowNotification(Long id, Long followerId, Long followeeId) {
        setId(id);
        this.followeeId = followeeId;
        this.followerId = followerId;
        this.checked = true;
    }

    @Column(name = "followee_id", nullable = false)
    private Long followeeId;

    @Column(name = "follower_id", nullable = false)
    private Long followerId;

    @Column(name = "checked", nullable = false)
    private boolean checked;

    @CreatedDate
    @Column(name = "created_date", nullable = false, updatable = false)
    private Instant createdDate = Instant.now();
}
