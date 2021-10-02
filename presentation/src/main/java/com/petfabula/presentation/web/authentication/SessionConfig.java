package com.petfabula.presentation.web.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.web.http.*;

@Configuration
public class SessionConfig {

//    @Bean
//    public HttpSessionIdResolver httpSessionIdResolver() {
//        return HeaderHttpSessionIdResolver.xAuthToken();
//    }

    @Bean
    public CookieSerializer cookieSerializer() {
        DefaultCookieSerializer serializer = new DefaultCookieSerializer();
        serializer.setCookieName("JSESSIONID");
        serializer.setCookiePath("/");
        serializer.setDomainNamePattern("^.+?\\.(\\w+\\.[a-z]+)$");
        serializer.setSameSite("none");
        return serializer;
    }

//    @Bean
//    public HttpSessionIdResolver httpSessionStrategy() {
//        return HeaderHttpSessionIdResolver.xAuthToken();
//    }

    @Bean
    public HttpSessionIdResolver httpSessionStrategy() {
        CookieHttpSessionIdResolver cookieHttpSessionIdResolver = new CookieHttpSessionIdResolver();
        cookieHttpSessionIdResolver.setCookieSerializer(cookieSerializer());

        return new HeaderAndCookieHttpSessionIdResolver(HeaderHttpSessionIdResolver.xAuthToken(),
                cookieHttpSessionIdResolver);
    }

    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher()
    {
        return new HttpSessionEventPublisher();
    }
}