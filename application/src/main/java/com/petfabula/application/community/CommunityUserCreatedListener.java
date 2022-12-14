package com.petfabula.application.community;

import com.petfabula.application.event.AccountCreatedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class CommunityUserCreatedListener {

    @Autowired
    private ParticipatorApplicationService participatorApplicationService;

    @EventListener
    public void handle(AccountCreatedEvent event) {
        participatorApplicationService
                .createParticipator(event.getId(), event.getName(), event.getPhoto());
    }
}
