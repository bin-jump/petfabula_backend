package com.petfabula.infrastructure.persistence.jpa.identity.repository;

import com.petfabula.domain.aggregate.identity.entity.ThirdPartyAuthentication;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ThirdPartyAuthenticationJpaRepository extends JpaRepository<ThirdPartyAuthentication, Long> {

    ThirdPartyAuthentication findByServerNameAndThirdPartyId(String thirdPartyId, String serverName);
}
