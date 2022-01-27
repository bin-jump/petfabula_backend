package com.petfabula.domain.aggregate.identity.repository;

import com.petfabula.domain.aggregate.identity.entity.EmailCodeRecord;

import java.util.List;

public interface EmailCodeRecordRepository {

    EmailCodeRecord save(EmailCodeRecord emailCodeRecord);

    EmailCodeRecord findByEmail(String email);

    List<EmailCodeRecord> findAll();

    long count();
}
