package com.petfabula.infrastructure.persistence.jpa.notification.repository;

import com.petfabula.domain.aggregate.notification.entity.UpvoteNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UpvoteJpaNotificationRepository extends JpaRepository<UpvoteNotification, Long>, JpaSpecificationExecutor {

    UpvoteNotification findByActorIdAndTargetEntityIdAndActionTypeAndTargetEntityType(Long actorId, Long entityId, UpvoteNotification.ActionType actionType,
                                    UpvoteNotification.EntityType entityType);
}
