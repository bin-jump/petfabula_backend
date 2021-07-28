package com.petfabula.domain.aggregate.notification.respository;

import com.petfabula.domain.aggregate.notification.entity.ParticipatorFollowNotification;
import com.petfabula.domain.common.paging.CursorPage;

public interface ParticipatorFollowNotificationRepository {

    ParticipatorFollowNotification findByAction(Long followerId, Long followeeId);

    ParticipatorFollowNotification save(ParticipatorFollowNotification followNotification);

    CursorPage<ParticipatorFollowNotification> findByFolloweeId(Long followeeId, Long cursor, int size);

    void remove(ParticipatorFollowNotification followNotification);
}
