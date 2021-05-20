package com.petfabula.infrastructure.oauth.google;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GoogleAccessTokenRequest {

    @JsonProperty("client_id")
    private String clientId;

    @JsonProperty("client_secret")
    private String clientSecret;

    @JsonProperty("code")
    private String code;

    @JsonProperty("redirect_uri")
    private String redirectUri;

    @JsonProperty("grant_type")
    private String grantType;
}
