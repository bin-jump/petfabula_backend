package com.petfabula.infrastructure.oauth.google;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GoogleUser {

    @JsonProperty("sub")
    private String authId;

    @JsonProperty("name")
    private String name;

    @JsonProperty("email")
    private String email;

    @JsonProperty("email_verified")
    private boolean emailVerified;
}