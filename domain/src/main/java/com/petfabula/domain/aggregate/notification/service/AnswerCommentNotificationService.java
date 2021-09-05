package com.petfabula.domain.aggregate.notification.service;

import com.petfabula.domain.aggregate.notification.entity.AnswerCommentNotification;
import com.petfabula.domain.aggregate.notification.entity.NotificationReceiver;
import com.petfabula.domain.aggregate.notification.respository.AnswerCommentNotificationRepository;
import com.petfabula.domain.aggregate.notification.respository.NotificationReceiverRepository;
import com.petfabula.domain.common.CommonMessageKeys;
import com.petfabula.domain.exception.InvalidOperationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AnswerCommentNotificationService {

    @Autowired
    private AnswerCommentNotificationRepository answerCommentNotificationRepository;

    @Autowired
    private NotificationReceiverRepository notificationReceiverRepository;

    @Autowired
    private NotificationIdGenerator notificationIdGenerator;

    public AnswerCommentNotification createNotification(Long ownerId, Long actorId, Long entityId,
                                                        AnswerCommentNotification.EntityType entityType,
                                                        Long targetEntityId,
                                                        AnswerCommentNotification.EntityType targetEntityType,
                                                        AnswerCommentNotification.ActionType actionType) {

        NotificationReceiver receiver = notificationReceiverRepository.findById(ownerId);
        if (receiver == null) {
            throw new InvalidOperationException(CommonMessageKeys.CANNOT_PROCEED);
        }
        if (!receiver.isReceiveUpvote()) {
            return null;
        }

        AnswerCommentNotification answerCommentNotification =
                answerCommentNotificationRepository.findByAction(entityId, entityType, targetEntityId, targetEntityType);

        if (answerCommentNotification == null) {
            Long id = notificationIdGenerator.nextId();
            answerCommentNotification = new AnswerCommentNotification(id, ownerId, actorId,
                    entityId, entityType, targetEntityId, targetEntityType, actionType);

            answerCommentNotification = answerCommentNotificationRepository.save(answerCommentNotification);
            notificationReceiverRepository.increateAnswerCommentUnreadCount(ownerId);
        }

        return answerCommentNotification;
    }

    public void readAll(Long receiverId) {
        notificationReceiverRepository.resetAnswerCommentUnreadCount(receiverId);
    }

}
