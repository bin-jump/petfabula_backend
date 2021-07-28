package com.petfabula.domain.aggregate.notification.service;

import com.petfabula.domain.aggregate.notification.entity.NotificationReceiver;
import com.petfabula.domain.aggregate.notification.respository.NotificationReceiverRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NotificationReceiverService {

    @Autowired
    private NotificationReceiverRepository notificationReceiverRepository;

    public NotificationReceiver create(Long id) {
        NotificationReceiver receiver = notificationReceiverRepository
                .findById(id);

        if (receiver != null) {
            return receiver;
        }

        receiver = new NotificationReceiver(id);
        return notificationReceiverRepository.save(receiver);
    }
}
