package com.petfabula.application.notification;

import com.petfabula.application.event.ParticipatorFollowEvent;
import com.petfabula.domain.aggregate.notification.service.ParticipatorFollowNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class NotificationParticipatorFollowListener {

    @Autowired
    private ParticipatorFollowNotificationService participatorFollowNotificationService;

    @Async
    @Transactional
    @TransactionalEventListener
    public void handle(ParticipatorFollowEvent event) {

        participatorFollowNotificationService
                .createNotification(event.getFollowerId(), event.getFolloweeId());
    }

}
