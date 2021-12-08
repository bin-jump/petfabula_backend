package com.petfabula.presentation.web.security.filter;

import com.petfabula.presentation.web.security.authencate.OauthRegisterToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class OauthRegisterFilter extends AbstractAuthenticationProcessingFilter {

    private static final AntPathRequestMatcher DEFAULT_ANT_PATH_REQUEST_MATCHER =
            new AntPathRequestMatcher("/api/identity/register-signin-oauth", "POST");

    public OauthRegisterFilter() {
        super(DEFAULT_ANT_PATH_REQUEST_MATCHER);
    }

    public OauthRegisterFilter(AuthenticationManager authenticationManager) {
        super(DEFAULT_ANT_PATH_REQUEST_MATCHER, authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws AuthenticationException, IOException, ServletException {
        if (!httpServletRequest.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + httpServletRequest.getMethod());
        } else {

            String serverName = httpServletRequest.getParameter("serverName");
            String code = httpServletRequest.getParameter("code");
            OauthRegisterToken authRequest = new OauthRegisterToken(code, serverName);
            return this.getAuthenticationManager().authenticate(authRequest);
        }
    }
}

