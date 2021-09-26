package com.petfabula.presentation.web.security.authencate;

import org.springframework.security.core.AuthenticationException;

public class AppAuthenticationException extends AuthenticationException {

    public AppAuthenticationException(Throwable cause) {
        super("", cause);
    }

    public AppAuthenticationException() {
        super("");
    }
}