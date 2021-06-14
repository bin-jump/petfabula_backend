package com.petfabula.application.community;

import com.petfabula.domain.common.event.UserCreated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class UserCreatedListener {

    @Autowired
    private ParticipatorApplicationService participatorApplicationService;

    @EventListener
    public void handle(UserCreated event) {
        participatorApplicationService
                .createParticipator(event.getId(), event.getName(), event.getPhoto());
    }
}
