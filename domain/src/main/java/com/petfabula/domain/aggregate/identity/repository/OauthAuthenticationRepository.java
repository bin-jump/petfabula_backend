package com.petfabula.domain.aggregate.identity.repository;

import com.petfabula.domain.aggregate.identity.entity.OauthAuthentication;

public interface OauthAuthenticationRepository {

    OauthAuthentication save(OauthAuthentication oauthAuthentication);

    OauthAuthentication findServerNameAndOauthId(String serverName, String oauthId);

}
