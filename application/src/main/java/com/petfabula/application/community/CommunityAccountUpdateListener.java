package com.petfabula.application.community;

import com.petfabula.application.event.AccountUpdateEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class CommunityAccountUpdateListener {

    @Autowired
    private ParticipatorApplicationService participatorApplicationService;

    @EventListener
    public void handle(AccountUpdateEvent event) {
        participatorApplicationService
                .updateParticipator(event.getId(), event.getName(), event.getPhoto(),
                        event.getBio(), event.getGender());
    }
}
