package com.petfabula.domain.aggregate.identity.repository;

import com.petfabula.domain.aggregate.identity.entity.ThirdPartyAuthentication;

public interface ThirdPartyAuthenticationRepository {

    ThirdPartyAuthentication save(ThirdPartyAuthentication thirdPartyAuthentication);

    ThirdPartyAuthentication findServerNameAndThirdPartyId(String serverName, String thirdPartyId);

}
