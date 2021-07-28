package com.petfabula.infrastructure.persistence.jpa.notification.repository;

import com.petfabula.domain.aggregate.notification.entity.AnswerCommentNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AnswerCommentNotificationJpaRepository extends JpaRepository<AnswerCommentNotification, Long>, JpaSpecificationExecutor {

    AnswerCommentNotification findByEntityIdAndEntityTypeAndTargetEntityIdAndTargetEntityType(Long entityId,
                                                                                              AnswerCommentNotification.EntityType entityType,
                                                                                              Long targetEntityId,
                                                                                              AnswerCommentNotification.EntityType targetEntityType);
}
