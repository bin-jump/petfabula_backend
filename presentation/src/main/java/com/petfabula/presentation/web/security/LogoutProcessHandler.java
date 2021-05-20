package com.petfabula.presentation.web.security;

import com.petfabula.presentation.web.authentication.AuthenticationHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LogoutProcessHandler implements LogoutHandler {

    @Autowired
    private AuthenticationHelper authenticationHelper;

    @Override
    public void logout(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) {
        authenticationHelper.logout(httpServletResponse);
    }
}
