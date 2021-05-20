package com.petfabula.domain.aggregate.identity.service.oauth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OauthResponse {

    private OauthServerName serverName;

    private String oauthId;

    private String userName;

    private String email;

    private String accessToken;
}
