package com.petfabula.domain.aggregate.notification.respository;

import com.petfabula.domain.aggregate.notification.entity.NotificationReceiver;

import java.time.Instant;

public interface NotificationReceiverRepository {

    NotificationReceiver findById(Long id);

    NotificationReceiver save(NotificationReceiver notificationReceiver);

    void increateAnswerCommentUnreadCount(Long id);

    void increateParticipatorFollowUnredCount(Long id);

    void increateVpvoteUnreadtCount(Long id);

    void resetAnswerCommentUnreadCount(Long id);

    void resetParticipatorFollowUnreadCount(Long id);

    void resetVpvoteUnreadtCount(Long id);

    void updateSystemNotificationReadTime(Long id, Instant time);
}
