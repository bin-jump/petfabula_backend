package com.petfabula.infrastructure.persistence.jpa.identity.impl;

import com.petfabula.domain.aggregate.identity.entity.ThirdPartyAuthentication;
import com.petfabula.domain.aggregate.identity.repository.ThirdPartyAuthenticationRepository;
import com.petfabula.infrastructure.persistence.jpa.identity.repository.ThirdPartyAuthenticationJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ThirdPartyAuthenticationRepositoryimpl implements ThirdPartyAuthenticationRepository {

    @Autowired
    private ThirdPartyAuthenticationJpaRepository thirdPartyAuthenticationJpaRepository;

    @Override
    public ThirdPartyAuthentication save(ThirdPartyAuthentication thirdPartyAuthentication) {
        return thirdPartyAuthenticationJpaRepository.save(thirdPartyAuthentication);
    }

    @Override
    public ThirdPartyAuthentication findServerNameAndThirdPartyId(String serverName, String thirdPartyId) {
        return thirdPartyAuthenticationJpaRepository.findByServerNameAndThirdPartyId(serverName, thirdPartyId);
    }
}
