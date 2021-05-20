package com.petfabula.domain.aggregate.identity.repository;

import com.petfabula.domain.aggregate.identity.entity.EmailPasswordAuthentication;

public interface EmailPasswordAuthenticationRepository {

    EmailPasswordAuthentication findByEmail(String email);

    EmailPasswordAuthentication save(EmailPasswordAuthentication emailPasswordAuthentication);
}
