package com.petfabula.infrastructure.oauth.google;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GoogleAccessTokenResponse {

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("expires_in")
    private int expireSecond;

    @JsonProperty("scope")
    private String scope;

    @JsonProperty("id_token")
    private String idToken;
}
