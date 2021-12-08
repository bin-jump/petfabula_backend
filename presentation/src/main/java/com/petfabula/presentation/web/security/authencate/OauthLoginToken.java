package com.petfabula.presentation.web.security.authencate;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.util.Assert;

import java.util.Collection;

public class OauthLoginToken extends AbstractAuthenticationToken {

    private final Object principal;
    private Object credentials;
    private String serverName;

    public OauthLoginToken(Object principal, String serverName) {
        super((Collection)null);
        this.principal = principal;
        this.serverName = serverName;
        this.setAuthenticated(false);
    }

    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        Assert.isTrue(!isAuthenticated, "Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
        super.setAuthenticated(false);
    }

    @Override
    public Object getCredentials() {
        return this.credentials;
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }

    public String getServerName() {
        return serverName;
    }
}
