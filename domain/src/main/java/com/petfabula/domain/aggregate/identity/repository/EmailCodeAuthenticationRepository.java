package com.petfabula.domain.aggregate.identity.repository;

import com.petfabula.domain.aggregate.identity.entity.EmailCodeAuthentication;

public interface EmailCodeAuthenticationRepository {

    EmailCodeAuthentication findByEmail(String email);

    EmailCodeAuthentication save(EmailCodeAuthentication emailCodeAuthentication);
}
