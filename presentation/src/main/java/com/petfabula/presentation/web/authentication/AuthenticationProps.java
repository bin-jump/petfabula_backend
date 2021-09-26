package com.petfabula.presentation.web.authentication;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

@Component
@ConfigurationProperties(prefix="app.authentication")
@Data
@NoArgsConstructor
public class AuthenticationProps {

    // for jwt
    @NotNull
    private String signSecret;

    @NotNull
    private String mobileRedirect;

    @NotNull
    private String webRedirect;
}
