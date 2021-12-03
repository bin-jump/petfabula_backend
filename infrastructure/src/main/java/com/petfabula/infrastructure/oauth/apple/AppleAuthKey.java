package com.petfabula.infrastructure.oauth.apple;

import lombok.Data;

@Data
public class AppleAuthKey {

    private String kty;

    private String kid;

    private String use;

    private String alg;

    private String n;

    private String e;
}
