package com.petfabula.infrastructure.oauth.core.props;

import lombok.Data;

@Data
public class ClientIdAndSerect {

    private String clientId;

    private String clientSerect;

    private String redirectUri;
}
