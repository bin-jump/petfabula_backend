package com.petfabula.domain.aggregate.identity.repository;

public interface VerificationCodeRepository {

    void save(String key, String code, int timeLimitSec);

    String getCode(String key);

    void remove(String key);
}
