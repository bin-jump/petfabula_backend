package com.petfabula.domain.aggregate.identity.repository;

import com.petfabula.domain.aggregate.identity.entity.EmailPasswordAuthentication;

public interface EmailPasswordAuthenticationRepository {

    EmailPasswordAuthentication findById(Long id);

    EmailPasswordAuthentication findByEmail(String email);

    EmailPasswordAuthentication save(EmailPasswordAuthentication emailPasswordAuthentication);
}
