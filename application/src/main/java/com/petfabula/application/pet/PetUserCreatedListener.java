package com.petfabula.application.pet;

import com.petfabula.domain.aggregate.pet.service.FeederService;
import com.petfabula.domain.common.event.UserCreated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class PetUserCreatedListener {

    @Autowired
    private FeederService feederService;

    @EventListener
    public void handle(UserCreated event) {
        feederService
                .create(event.getId(), event.getName(), event.getPhoto());
    }
}
