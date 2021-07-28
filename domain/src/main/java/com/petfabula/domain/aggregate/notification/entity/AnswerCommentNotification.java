package com.petfabula.domain.aggregate.notification.entity;

import com.petfabula.domain.common.domain.EntityBase;
import com.petfabula.domain.exception.InvalidOperationException;
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
@Table(name = "notification_answer_comment", uniqueConstraints={
        @UniqueConstraint(columnNames = {"entity_id", "entity_type", "target_entity_id", "target_entity_type"})})
public class AnswerCommentNotification extends EntityBase {

    public AnswerCommentNotification(Long id, Long ownerId, Long actorId, Long entityId,
                                     EntityType entityType, Long targetEntityId, EntityType targetEntityType, ActionType actionType) {
        if (ActionType.ANSWER.equals(actionType) && !EntityType.QUESTION.equals(targetEntityType)) {
            throw new InvalidOperationException("");
        }
        setId(id);
        this.actorId = actorId;
        this.ownerId = ownerId;
        this.entityId = entityId;
        this.targetEntityId = targetEntityId;
        this.entityType = entityType;
        this.targetEntityType = targetEntityType;
        this.actionType = actionType;
        this.checked = true;
    }

    @Column(name = "owner_id", nullable = false)
    private Long ownerId;

    @Column(name = "actor_id", nullable = false)
    private Long actorId;

    @Column(name = "entity_id", nullable = false)
    private Long entityId;

    @Column(name = "target_entity_id", nullable = false)
    private Long targetEntityId;

    @Column(name = "checked", nullable = false)
    private boolean checked;

    @Column(name = "entity_type", nullable = false)
    private EntityType entityType;

    @Column(name = "target_entity_type", nullable = false)
    private EntityType targetEntityType;

    @Column(name = "action_type", nullable = false)
    private ActionType actionType;

    @CreatedDate
    @Column(name = "created_date", nullable = false, updatable = false)
    private Instant createdDate = Instant.now();


    public enum EntityType {
        QUESTION, ANSWER, POST, QUESTION_COMMENT, ANSWER_COMMENT, POST_COMMENT, POST_COMMENT_REPLY
    }

    public enum ActionType {
        ANSWER, COMMENT, REPLY
    }
}
