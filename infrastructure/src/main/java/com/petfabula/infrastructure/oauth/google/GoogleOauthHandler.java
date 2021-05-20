package com.petfabula.infrastructure.oauth.google;

import com.petfabula.domain.aggregate.identity.service.oauth.OauthRequest;
import com.petfabula.domain.aggregate.identity.service.oauth.OauthResponse;
import com.petfabula.domain.aggregate.identity.service.oauth.OauthServerName;
import com.petfabula.domain.exception.InvalidOauthException;
import com.petfabula.infrastructure.oauth.core.OauthHandler;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

@Component
public class GoogleOauthHandler extends OauthHandler {

    @Override
    public OauthResponse doAuth(OauthRequest request) {
        GoogleAccessTokenRequest req = GoogleAccessTokenRequest.builder()
                .clientId(oauthProps.getGoogle().getClientId())
                .clientSecret(oauthProps.getGoogle().getClientSerect())
                .code(request.getCode())
                .redirectUri(oauthProps.getRedirectUrl())
                .grantType("authorization_code")
                .build();

        OauthResponse authInfo = new OauthResponse();
        GoogleAccessTokenResponse res = restTemplate
                .postForObject("https://oauth2.googleapis.com/token",
                        req, GoogleAccessTokenResponse.class);
        authInfo.setAccessToken(res.getAccessToken());

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", String.format("Bearer %s", res.getAccessToken()));
        GoogleUser user = requestWithHeader("https://www.googleapis.com/oauth2/v3/userinfo",
                HttpMethod.GET, headers, GoogleUser.class);

        if (user.isEmailVerified()) {
            authInfo.setEmail(user.getEmail());
        }
        authInfo.setOauthId(user.getAuthId());
        authInfo.setUserName(user.getName());
        return authInfo;
    }

    @Override
    public boolean support(OauthServerName serverName) {
        return OauthServerName.GOOGLE == serverName;
    }
}
