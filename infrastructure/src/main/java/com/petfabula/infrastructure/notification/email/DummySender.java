package com.petfabula.infrastructure.notification.email;

import com.petfabula.domain.aggregate.identity.service.email.EmailSender;
import com.petfabula.domain.aggregate.identity.service.email.SendEmailRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

//@Component
@Slf4j
public class DummySender implements EmailSender {

    @PostConstruct
    public void init() {
        log.warn("Dummy email sender is used");
    }

    @Override
    public void sendEmail(SendEmailRequest sendEmailRequest) {
        log.info("Dummy email sender send to " + sendEmailRequest.getAddress()
                + " mail: " + sendEmailRequest.getContent());
    }
}
