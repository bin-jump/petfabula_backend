package com.petfabula.infrastructure.persistence.jpa.identity.impl;

import com.petfabula.domain.aggregate.identity.entity.EmailCodeRecord;
import com.petfabula.domain.aggregate.identity.repository.EmailCodeRecordRepository;
import com.petfabula.infrastructure.persistence.jpa.identity.repository.EmailCodeRecordJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class EmailCodeRecordRepositoryImpl implements EmailCodeRecordRepository {

    @Autowired
    private EmailCodeRecordJpaRepository emailCodeRecordJpaRepository;

    @Override
    public EmailCodeRecord save(EmailCodeRecord emailCodeRecord) {
        return emailCodeRecordJpaRepository.save(emailCodeRecord);
    }

    @Override
    public EmailCodeRecord findByEmail(String email) {
        return emailCodeRecordJpaRepository.findByEmail(email);
    }

    @Override
    public List<EmailCodeRecord> findAll() {
        return emailCodeRecordJpaRepository.findAll();
    }

    @Override
    public long count() {
        return emailCodeRecordJpaRepository.count();
    }
}
