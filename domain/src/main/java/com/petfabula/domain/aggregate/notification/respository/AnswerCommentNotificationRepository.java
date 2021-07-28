package com.petfabula.domain.aggregate.notification.respository;

import com.petfabula.domain.aggregate.notification.entity.AnswerCommentNotification;
import com.petfabula.domain.common.paging.CursorPage;

public interface AnswerCommentNotificationRepository {

    AnswerCommentNotification findByAction(Long entityId,
                                           AnswerCommentNotification.EntityType entityType,
                                           Long targetEntityId,
                                           AnswerCommentNotification.EntityType targetEntityType);

    AnswerCommentNotification save(AnswerCommentNotification answerCommentNotification);

    CursorPage<AnswerCommentNotification> findByOwnerId(Long ownerId, Long cursor, int size);

    void remove(AnswerCommentNotification answerCommentNotification);
}
