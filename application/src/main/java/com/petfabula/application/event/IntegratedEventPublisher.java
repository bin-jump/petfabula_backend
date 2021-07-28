package com.petfabula.application.event;

import com.petfabula.domain.common.domain.DomainEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class IntegratedEventPublisher {

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    public void publish(final Object event) {
        applicationEventPublisher.publishEvent(event);
    }
}
