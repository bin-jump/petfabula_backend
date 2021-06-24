package com.petfabula.infrastructure.oauth;

import com.petfabula.domain.aggregate.identity.service.oauth.OauthRequest;
import com.petfabula.domain.aggregate.identity.service.oauth.OauthResponse;
import com.petfabula.domain.aggregate.identity.service.oauth.OauthServer;
import com.petfabula.domain.exception.InvalidOauthException;
import com.petfabula.infrastructure.oauth.core.OauthHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OauthServerImpl implements OauthServer {

    @Autowired
    List<OauthHandler> handlers;

    @Override
    public OauthResponse doOauth(OauthRequest oauthRequest) {
        for (OauthHandler h : handlers) {
            if (h.support(oauthRequest.getServerName())) {
                OauthResponse res = h.doAuth(oauthRequest);
                if (!isValid(res)) {
                    throw new InvalidOauthException("Can not get necessary information");
                }
                return res;
            }
        }

        // should not come here
        throw new RuntimeException("Bad oauth request");
    }

    public boolean isValid(OauthResponse response) {
        return response.getUserName() != null &&
                response.getEmail() != null &&
                response.getOauthId() != null;
    }

}
