package com.petfabula.application.notification;

import com.petfabula.application.event.AccountCreatedEvent;
import com.petfabula.domain.aggregate.notification.service.NotificationReceiverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class NotificationUserCreatedListener {

    @Autowired
    private NotificationReceiverService notificationReceiverService;

    @EventListener
    public void handle(AccountCreatedEvent event) {
        notificationReceiverService
                .create(event.getId());
    }
}
