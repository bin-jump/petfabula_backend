package com.petfabula.infrastructure.persistence.jpa.identity.repository;

import com.petfabula.domain.aggregate.identity.entity.OauthAuthentication;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OauthAuthenticationJpaRepository extends JpaRepository<OauthAuthentication, Long> {

    OauthAuthentication findByServerNameAndOauthId(String oauthId, String serverName);
}
