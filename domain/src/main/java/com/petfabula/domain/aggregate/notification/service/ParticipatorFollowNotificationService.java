package com.petfabula.domain.aggregate.notification.service;

import com.petfabula.domain.aggregate.notification.entity.ParticipatorFollowNotification;
import com.petfabula.domain.aggregate.notification.respository.NotificationReceiverRepository;
import com.petfabula.domain.aggregate.notification.respository.ParticipatorFollowNotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ParticipatorFollowNotificationService {

    @Autowired
    private ParticipatorFollowNotificationRepository participatorFollowNotificationRepository;

    @Autowired
    private NotificationReceiverRepository notificationReceiverRepository;

    @Autowired
    private NotificationIdGenerator notificationIdGenerator;

    public ParticipatorFollowNotification createNotification(Long followerId, Long followeeId) {

        ParticipatorFollowNotification followNotification =
                participatorFollowNotificationRepository.findByAction(followerId, followeeId);

        if (followNotification == null) {
            Long id = notificationIdGenerator.nextId();
            followNotification = new ParticipatorFollowNotification(id, followerId, followeeId);
            followNotification = participatorFollowNotificationRepository.save(followNotification);
            notificationReceiverRepository.increateParticipatorFollowUnredCount(followeeId);
        }

        return followNotification;
    }

    public void readAll(Long receiverId) {
        notificationReceiverRepository.resetParticipatorFollowUnreadCount(receiverId);
    }
}
