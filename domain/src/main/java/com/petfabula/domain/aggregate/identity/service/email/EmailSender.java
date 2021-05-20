package com.petfabula.domain.aggregate.identity.service.email;

public interface EmailSender {

    void sendEmail(SendEmailRequest sendEmailRequest);

}
