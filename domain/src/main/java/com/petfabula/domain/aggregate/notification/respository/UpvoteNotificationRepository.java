package com.petfabula.domain.aggregate.notification.respository;

import com.petfabula.domain.aggregate.notification.entity.UpvoteNotification;
import com.petfabula.domain.common.paging.CursorPage;

public interface UpvoteNotificationRepository {

    UpvoteNotification findByAction(Long actorId, Long entityId,
                                    UpvoteNotification.ActionType actionType, UpvoteNotification.EntityType entityType);

    UpvoteNotification save(UpvoteNotification upvoteNotification);

    CursorPage<UpvoteNotification> findByOwnerId(Long ownerId, Long cursor, int size);

    void remove(UpvoteNotification upvoteNotification);
}
