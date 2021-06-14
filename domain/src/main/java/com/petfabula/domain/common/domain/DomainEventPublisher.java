package com.petfabula.domain.common.domain;

import com.petfabula.domain.common.domain.DomainEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class DomainEventPublisher {

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    public <T extends DomainEvent> void publish(final T aDomainEvent) {
        applicationEventPublisher.publishEvent(aDomainEvent);
    }

}
