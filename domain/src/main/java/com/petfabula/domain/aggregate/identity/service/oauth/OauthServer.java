package com.petfabula.domain.aggregate.identity.service.oauth;

import com.petfabula.domain.common.validation.EntityValidationUtils;

public interface OauthServer {

    OauthResponse doOauth(OauthRequest oauthRequest);

}
