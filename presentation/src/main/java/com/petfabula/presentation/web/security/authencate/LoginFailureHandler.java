package com.petfabula.presentation.web.security.authencate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.petfabula.domain.aggregate.identity.MessageKey;
import com.petfabula.domain.exception.DomainAuthenticationException;
import com.petfabula.domain.exception.InvalidValueException;
import com.petfabula.presentation.web.api.Response;
import com.petfabula.presentation.web.error.GlobalExceptionTranslator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class LoginFailureHandler implements AuthenticationFailureHandler {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private GlobalExceptionTranslator globalExceptionTranslator;

    @Autowired
    ResourceBundleMessageSource messageSource;

    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setContentType("application/json;charset=UTF-8");

        if (e instanceof SessionAuthenticationException) {
            httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());

            Response res = Response.failed(Response.ResponseCode.AUTHORIZATION_LIMIT_REACHED);
            res.setMessage(messageSource.getMessage(Response.ResponseCode.AUTHORIZATION_LIMIT_REACHED.getMessage(),
                    null, LocaleContextHolder.getLocale()));

            httpServletResponse.getWriter().write(objectMapper.writeValueAsString(res));
        } else if (e.getCause() instanceof DomainAuthenticationException) {
            Response res = globalExceptionTranslator
                    .handleDomainAuthenticationException((DomainAuthenticationException)e.getCause(), httpServletRequest);
            httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
            httpServletResponse.getWriter().write(objectMapper.writeValueAsString(res));
        } else if (e.getCause() instanceof InvalidValueException) {
            Response res = globalExceptionTranslator
                    .handleInvalidValueException((InvalidValueException)e.getCause(), httpServletRequest);
            httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
            httpServletResponse.getWriter().write(objectMapper.writeValueAsString(res));
        } else {
            Response res = Response.failedOnValidation();
            res.setMessage(messageSource.getMessage(Response.ResponseCode.AUTHENTICATION_FAILED.getMessage(),
                    null, LocaleContextHolder.getLocale()));

            httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
            httpServletResponse.getWriter().write(objectMapper.writeValueAsString(res));
        }
    }
}
