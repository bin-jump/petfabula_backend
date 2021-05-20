package com.petfabula.infrastructure.oauth.core.props;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Component
@ConfigurationProperties(prefix="app.oauth")
@Data
@NoArgsConstructor
@Validated
public class OauthProps {

    @NotNull
    private String redirectUrl;

    @NotNull
    private ClientIdAndSerect github;

    @NotNull
    private ClientIdAndSerect google;

    private ClientIdAndSerect facebook;
}