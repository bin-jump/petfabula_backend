package com.petfabula.infrastructure.persistence.jpa.identity.impl;

import com.petfabula.domain.aggregate.identity.entity.EmailPasswordAuthentication;
import com.petfabula.domain.aggregate.identity.repository.EmailPasswordAuthenticationRepository;
import com.petfabula.infrastructure.persistence.jpa.identity.repository.EmailAuthenticationJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class EmailPasswordAuthenticationRepositoryImpl implements EmailPasswordAuthenticationRepository {

    @Autowired
    private EmailAuthenticationJpaRepository emailAuthenticationJpaRepository;

    @Override
    public EmailPasswordAuthentication findById(Long id) {
        return emailAuthenticationJpaRepository.findById(id).orElse(null);
    }

    @Override
    public EmailPasswordAuthentication findByEmail(String email) {
        return emailAuthenticationJpaRepository.findByEmail(email);
    }

    @Override
    public EmailPasswordAuthentication save(EmailPasswordAuthentication emailPasswordAuthentication) {
        return emailAuthenticationJpaRepository.save(emailPasswordAuthentication);
    }
}
