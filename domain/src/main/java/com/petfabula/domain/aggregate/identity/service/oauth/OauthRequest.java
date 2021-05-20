package com.petfabula.domain.aggregate.identity.service.oauth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OauthRequest {

    private OauthServerName serverName;

    private String code;
}
