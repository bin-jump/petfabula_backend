package com.petfabula.domain.aggregate.identity;

import com.petfabula.domain.aggregate.identity.service.VerificationCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class UserCreatedListener {

    @Autowired
    private VerificationCodeService verificationCodeService;

    @TransactionalEventListener
    @Async
    public void handleUserCreated(IdentityCreated identityCreated) {
        verificationCodeService.removeEmailCodeRegisterCode(identityCreated.getEmail());
    }
}
