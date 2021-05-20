package com.petfabula.infrastructure.persistence.jpa.identity.impl;

import com.petfabula.domain.aggregate.identity.entity.OauthAuthentication;
import com.petfabula.domain.aggregate.identity.repository.OauthAuthenticationRepository;
import com.petfabula.infrastructure.persistence.jpa.identity.repo.OauthAuthenticationJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class OauthAuthenticationRepositoryimpl implements OauthAuthenticationRepository {

    @Autowired
    private OauthAuthenticationJpaRepository oauthAuthenticationJpaRepository;

    @Override
    public OauthAuthentication save(OauthAuthentication oauthAuthentication) {
        return oauthAuthenticationJpaRepository.save(oauthAuthentication);
    }

    @Override
    public OauthAuthentication findServerNameAndOauthId(String serverName, String oauthId) {
        return oauthAuthenticationJpaRepository.findByServerNameAndOauthId(serverName, oauthId);
    }
}
