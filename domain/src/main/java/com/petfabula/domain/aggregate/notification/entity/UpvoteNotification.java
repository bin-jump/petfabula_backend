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
@Table(name = "notification_vote", uniqueConstraints={
        @UniqueConstraint(columnNames = {"actor_id", "target_entity_id", "target_entity_type", "action_type"})})
public class UpvoteNotification extends EntityBase {

    public UpvoteNotification(Long id, Long ownerId, Long actorId, Long targetEntityId,
                              EntityType targetEntityType, ActionType actionType) {
        setId(id);
        this.actorId = actorId;
        this.ownerId = ownerId;
        this.targetEntityId = targetEntityId;
        this.targetEntityType = targetEntityType;
        this.actionType = actionType;
        this.checked = true;
    }

    @Column(name = "owner_id", nullable = false)
    private Long ownerId;

    @Column(name = "actor_id", nullable = false)
    private Long actorId;

    @Column(name = "target_entity_id", nullable = false)
    private Long targetEntityId;

    @Column(name = "checked", nullable = false)
    private boolean checked;

    @Column(name = "target_entity_type", nullable = false)
    private EntityType targetEntityType;

    @Column(name = "action_type", nullable = false)
    private ActionType actionType;

    @CreatedDate
    @Column(name = "created_date", nullable = false, updatable = false)
    private Instant createdDate = Instant.now();


    public enum EntityType {
        POST, QUESTION, ANSWER
    }

    public enum ActionType {
        UPVOTE, COLLECT
    }
}
