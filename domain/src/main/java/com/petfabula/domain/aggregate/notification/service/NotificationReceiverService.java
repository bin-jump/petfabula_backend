package com.petfabula.domain.aggregate.notification.service;

import com.petfabula.domain.aggregate.notification.entity.NotificationReceiver;
import com.petfabula.domain.aggregate.notification.respository.NotificationReceiverRepository;
import com.petfabula.domain.common.CommonMessageKeys;
import com.petfabula.domain.exception.InvalidOperationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NotificationReceiverService {

    @Autowired
    private NotificationReceiverRepository notificationReceiverRepository;

    public NotificationReceiver create(Long id) {
        NotificationReceiver receiver = notificationReceiverRepository.findById(id);

        if (receiver != null) {
            return receiver;
        }

        receiver = new NotificationReceiver(id);
        return notificationReceiverRepository.save(receiver);
    }

    public void updateSetting(Long receiverId, boolean receiveAnswerComment, boolean receiveFollow, boolean receiveUpvote) {
        NotificationReceiver receiver = notificationReceiverRepository.findById(receiverId);

        if (receiver == null) {
            throw new InvalidOperationException(CommonMessageKeys.CANNOT_PROCEED);
        }

        receiver.setReceiveAnswerComment(receiveAnswerComment);
        receiver.setReceiveFollow(receiveFollow);
        receiver.setReceiveUpvote(receiveUpvote);

        notificationReceiverRepository.save(receiver);
    }
}
