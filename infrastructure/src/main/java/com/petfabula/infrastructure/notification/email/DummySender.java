package com.petfabula.infrastructure.notification.email;

import com.petfabula.domain.aggregate.identity.service.email.EmailSender;
import com.petfabula.domain.aggregate.identity.service.email.SendEmailRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DummySender implements EmailSender {

    @Override
    public void sendEmail(SendEmailRequest sendEmailRequest) {
        log.info("Dummy email sender send to " + sendEmailRequest.getAddress());
    }
}
