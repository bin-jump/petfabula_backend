package com.petfabula.infrastructure.persistence.jpa.identity.repository;

import com.petfabula.domain.aggregate.identity.entity.EmailCodeAuthentication;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailCodeAuthenticationJpaRepository extends JpaRepository<EmailCodeAuthentication, Long> {

    EmailCodeAuthentication findByEmail(String email);

}
