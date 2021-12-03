package com.petfabula.infrastructure.oauth.apple;

import com.petfabula.domain.aggregate.identity.service.oauth.AppleAuthContent;
import com.petfabula.domain.aggregate.identity.service.oauth.AppleService;
import com.petfabula.domain.exception.InvalidOauthException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AppleServiceImpl implements AppleService {

    @Autowired
    private AppleJwtDecoder appleJwtDecoder;

    @Override
    public AppleAuthContent validContentFromJwt(String jwtToken) {
        AppleJwtPayload payload = appleJwtDecoder.decode(jwtToken);
        if (!payload.getEmailVerified()) {
            throw new InvalidOauthException("Can not get necessary information");
        }

        return new AppleAuthContent(payload.getEmail(), payload.getSub());
    }
}
