package com.petfabula.presentation.web.error;

import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.petfabula.domain.exception.InvalidOperationException;
import com.petfabula.domain.exception.InvalidValueException;
import com.petfabula.presentation.web.api.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * for error outside spring
 */
@Slf4j
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ExceptionHandlerFilter extends OncePerRequestFilter {

    @Autowired
    ResourceBundleMessageSource messageSource;

    @Autowired
    private GlobalExceptionTranslator globalExceptionTranslator;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        try{
            filterChain.doFilter(request, response);
        } catch (HttpMessageNotReadableException | SignatureVerificationException ex) {
            ex.printStackTrace();
            Response res = Response.failed(Response.ResponseCode.INVALID_OPERATION);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write(objectMapper.writeValueAsString(res));
        } catch (InvalidOperationException ex) {
//            Response res = Response.failed(Response.ResponseCode.INVALID_OPERATION);
//            res.setMessage(messageSource.getMessage(ex.getMessage(), null, LocaleContextHolder.getLocale()));
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            Response res = globalExceptionTranslator.handleInvalidOperationException(ex, request);
            response.getWriter().write(objectMapper.writeValueAsString(res));
        } catch (InvalidValueException ex) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            Response res = globalExceptionTranslator.handleInvalidValueException(ex, request);
            response.getWriter().write(objectMapper.writeValueAsString(res));
        } catch (AccessDeniedException ex) {
            Response res = Response.failed(Response.ResponseCode.AUTHORIZATION_FAILED);
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write(objectMapper.writeValueAsString(res));
        } catch (RuntimeException ex) {
            ex.printStackTrace();
            Response res = Response.failedInternal();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(objectMapper.writeValueAsString(res));
        } catch (Exception ex) {
            ex.printStackTrace();
            Response res = Response.failedInternal();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(objectMapper.writeValueAsString(res));
        }

    }
}
