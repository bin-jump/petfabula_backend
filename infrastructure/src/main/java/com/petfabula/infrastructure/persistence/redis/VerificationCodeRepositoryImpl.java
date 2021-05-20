package com.petfabula.infrastructure.persistence.redis;

import com.petfabula.domain.aggregate.identity.repository.VerificationCodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.time.Duration;

@Repository
public class VerificationCodeRepositoryImpl implements VerificationCodeRepository {

    @Autowired
    private RedisTemplate<String, String> template;


    @Override
    public void save(String key, String code, int timeLimitSec) {
        template.opsForValue().set(key, code, Duration.ofSeconds(timeLimitSec));
    }

    @Override
    public String getCode(String key) {
        return template.opsForValue().get(key);
    }

    @Override
    public void remove(String key) {
        template.delete(key);
    }
}
