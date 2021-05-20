package com.petfabula.infrastructure.persistence.jpa.identity.impl;

import com.petfabula.domain.aggregate.identity.entity.EmailCodeAuthentication;
import com.petfabula.domain.aggregate.identity.repository.EmailCodeAuthenticationRepository;
import com.petfabula.infrastructure.persistence.jpa.identity.repo.EmailCodeAuthenticationJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class EmailCodeAuthenticationRepositoryImpl implements EmailCodeAuthenticationRepository {

    @Autowired
    private EmailCodeAuthenticationJpaRepository emailCodeAuthenticationJpaRepository;

    @Override
    public EmailCodeAuthentication findByEmail(String email) {
        return emailCodeAuthenticationJpaRepository.findByEmail(email);
    }

    @Override
    public EmailCodeAuthentication save(EmailCodeAuthentication emailCodeAuthentication) {
        return emailCodeAuthenticationJpaRepository.save(emailCodeAuthentication);
    }
}
