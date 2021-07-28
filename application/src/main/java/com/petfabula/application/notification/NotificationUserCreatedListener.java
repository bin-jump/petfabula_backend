package com.petfabula.application.notification;

import com.petfabula.domain.aggregate.notification.service.NotificationReceiverService;
import com.petfabula.domain.common.event.UserCreated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class NotificationUserCreatedListener {

    @Autowired
    private NotificationReceiverService notificationReceiverService;

    @EventListener
    public void handle(UserCreated event) {
        notificationReceiverService
                .create(event.getId());
    }
}
