package com.petfabula.presentation.web.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.session.web.http.CookieHttpSessionIdResolver;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.HeaderHttpSessionIdResolver;
import org.springframework.session.web.http.HttpSessionIdResolver;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class HeaderAndCookieHttpSessionIdResolver implements HttpSessionIdResolver {

    private HeaderHttpSessionIdResolver headerHttpSessionIdResolver;

    private CookieHttpSessionIdResolver cookieHttpSessionIdResolver;

    public HeaderAndCookieHttpSessionIdResolver(HeaderHttpSessionIdResolver headerHttpSessionIdResolver,
                                                CookieHttpSessionIdResolver cookieHttpSessionIdResolver) {
        this.headerHttpSessionIdResolver = headerHttpSessionIdResolver;
        this.cookieHttpSessionIdResolver = cookieHttpSessionIdResolver;
    }

    @Override
    public List<String> resolveSessionIds(HttpServletRequest httpServletRequest) {
        List<String> headerIds = headerHttpSessionIdResolver.resolveSessionIds(httpServletRequest);
        List<String> cookieIds = cookieHttpSessionIdResolver.resolveSessionIds(httpServletRequest);
        List<String> sessionIds = Stream.concat(headerIds.stream(),cookieIds.stream())
                .distinct()
                .collect(Collectors.toList());
        return sessionIds;
    }

    @Override
    public void setSessionId(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, String s) {
        headerHttpSessionIdResolver.setSessionId(httpServletRequest, httpServletResponse, s);
        cookieHttpSessionIdResolver.setSessionId(httpServletRequest, httpServletResponse, s);
    }

    @Override
    public void expireSession(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        headerHttpSessionIdResolver.expireSession(httpServletRequest, httpServletResponse);
        cookieHttpSessionIdResolver.expireSession(httpServletRequest, httpServletResponse);
    }
}
