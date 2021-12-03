package com.petfabula.domain.aggregate.identity.service.oauth;

public interface AppleService {

    String SERVER_NAME = "APPLE";

    AppleAuthContent validContentFromJwt(String jwtToken);
}
