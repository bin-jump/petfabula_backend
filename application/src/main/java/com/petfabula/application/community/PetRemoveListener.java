package com.petfabula.application.community;

import com.petfabula.application.event.PetRemoveEvent;
import com.petfabula.domain.aggregate.community.participator.service.ParticipatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class PetRemoveListener {

    @Autowired
    private ParticipatorService participatorService;

    @EventListener
    public void handle(PetRemoveEvent event) {
        participatorService.removePet(event.getPetId());
    }
}
