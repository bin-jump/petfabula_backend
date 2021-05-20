package com.petfabula.infrastructure.oauth.core;

import com.petfabula.domain.aggregate.identity.service.oauth.OauthRequest;
import com.petfabula.domain.aggregate.identity.service.oauth.OauthResponse;
import com.petfabula.domain.aggregate.identity.service.oauth.OauthServerName;
import com.petfabula.infrastructure.oauth.core.props.OauthProps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

public abstract class OauthHandler {

    @Autowired
    protected RestTemplate restTemplate;

    @Autowired
    protected OauthProps oauthProps;

    public abstract OauthResponse doAuth(OauthRequest request);

    public abstract boolean support(OauthServerName serverName);

    protected  <T> T requestWithHeader(String url, HttpMethod method, HttpHeaders headers, Class<T> responseType) {
        HttpEntity<?> reqEntity = new HttpEntity<>(headers);
        HttpEntity<T> resEntity = restTemplate
                .exchange(url, method, reqEntity, responseType);

        return resEntity.getBody();
    }
}