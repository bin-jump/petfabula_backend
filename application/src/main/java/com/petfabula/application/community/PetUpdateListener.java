package com.petfabula.application.community;

import com.petfabula.application.event.PetUpdateEvent;
import com.petfabula.domain.aggregate.community.participator.service.ParticipatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class PetUpdateListener {

    @Autowired
    private ParticipatorService participatorService;

    @EventListener
    public void handle(PetUpdateEvent event) {
        participatorService
                .updatePet(event.getPet().getId(), event.getPet().getFeederId(),
                        event.getPet().getName(), event.getPet().getPhoto(),
                        event.getPet().getCategory(), event.getPet().getBreedId());
    }
}
