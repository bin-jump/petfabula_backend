package com.petfabula.infrastructure.persistence.jpa.notification.impl;

import com.petfabula.domain.aggregate.notification.entity.NotificationReceiver;
import com.petfabula.domain.aggregate.notification.respository.NotificationReceiverRepository;
import com.petfabula.infrastructure.persistence.jpa.notification.repository.NotificationReceiverJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.Instant;

@Repository
public class NotificationReceiverRepositoryImpl implements NotificationReceiverRepository {

    @Autowired
    private NotificationReceiverJpaRepository notificationReceiverJpaRepository;

    @Override
    public NotificationReceiver findById(Long id) {
        return notificationReceiverJpaRepository.findById(id).orElse(null);
    }

    @Override
    public NotificationReceiver save(NotificationReceiver notificationReceiver) {
        return notificationReceiverJpaRepository.save(notificationReceiver);
    }

    @Override
    public void increateAnswerCommentUnreadCount(Long id) {
        notificationReceiverJpaRepository.increateAnswerCommentUnreadCount(id);
    }

    @Override
    public void increateParticipatorFollowUnredCount(Long id) {
        notificationReceiverJpaRepository.increateParticipatorFollowUnredCount(id);
    }

    @Override
    public void increateVpvoteUnreadtCount(Long id) {
        notificationReceiverJpaRepository.increateVpvoteUnreadtCount(id);
    }

    @Override
    public void resetAnswerCommentUnreadCount(Long id) {
        notificationReceiverJpaRepository.resetAnswerCommentUnreadCount(id);
    }

    @Override
    public void resetParticipatorFollowUnreadCount(Long id) {
        notificationReceiverJpaRepository.resetParticipatorFollowUnreadCount(id);
    }

    @Override
    public void resetVpvoteUnreadtCount(Long id) {
        notificationReceiverJpaRepository.resetVpvoteUnreadtCount(id);
    }

    @Override
    public void updateSystemNotificationReadTime(Long id, Instant time) {
        notificationReceiverJpaRepository.updateSystemNotificationReadTime(id, time);
    }
}
