package com.petfabula.presentation.web.security.authencate;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class AppleToken extends AbstractAuthenticationToken {

    private Object principal;
    private Object credentials;

    private AppleToken(Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
    }

    public AppleToken(Object principal, Object credentials) {
        super((Collection)null);
        this.principal = principal;
        this.credentials = credentials;
        this.setAuthenticated(false);
    }

    @Override
    public Object getCredentials() {
        return credentials;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }
}
