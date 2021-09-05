package com.petfabula.domain.aggregate.notification.service;

import com.petfabula.domain.aggregate.notification.entity.NotificationReceiver;
import com.petfabula.domain.aggregate.notification.entity.UpvoteNotification;
import com.petfabula.domain.aggregate.notification.respository.NotificationReceiverRepository;
import com.petfabula.domain.aggregate.notification.respository.UpvoteNotificationRepository;
import com.petfabula.domain.common.CommonMessageKeys;
import com.petfabula.domain.exception.InvalidOperationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VoteNotificationService {

    @Autowired
    private UpvoteNotificationRepository upvoteNotificationRepository;

    @Autowired
    private NotificationReceiverRepository notificationReceiverRepository;

    @Autowired
    private NotificationIdGenerator notificationIdGenerator;

    public UpvoteNotification createNotification(Long ownerId, Long actorId, Long targetEntityId,
                                                 UpvoteNotification.EntityType targetEntityType,
                                                 UpvoteNotification.ActionType actionType) {

        NotificationReceiver receiver = notificationReceiverRepository.findById(ownerId);
        if (receiver == null) {
            throw new InvalidOperationException(CommonMessageKeys.CANNOT_PROCEED);
        }
        if (!receiver.isReceiveUpvote()) {
            return null;
        }

        UpvoteNotification upvoteNotification =
                upvoteNotificationRepository.findByAction(actorId, targetEntityId, actionType, targetEntityType);
        if (upvoteNotification == null) {
            Long id = notificationIdGenerator.nextId();
            upvoteNotification = new UpvoteNotification(id, ownerId, actorId,
                    targetEntityId, targetEntityType, actionType);

            upvoteNotification = upvoteNotificationRepository.save(upvoteNotification);
            notificationReceiverRepository.increateVpvoteUnreadtCount(ownerId);
        }

        return upvoteNotification;
    }

    public void readAll(Long receiverId) {
        notificationReceiverRepository.resetVpvoteUnreadtCount(receiverId);
    }

}
