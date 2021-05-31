package com.petfabula.infrastructure.persistence.jpa.identity.repository;

import com.petfabula.domain.aggregate.identity.entity.EmailPasswordAuthentication;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailAuthenticationJpaRepository extends JpaRepository<EmailPasswordAuthentication, Long> {

    EmailPasswordAuthentication findByEmail(String email);

}
