package com.petfabula.presentation.facade.dto.identity;

import com.petfabula.domain.aggregate.identity.service.oauth.OauthServerName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OauthRequest {

    @NotNull
    private String code;

    @NotNull
    private OauthServerName serverName;
}
