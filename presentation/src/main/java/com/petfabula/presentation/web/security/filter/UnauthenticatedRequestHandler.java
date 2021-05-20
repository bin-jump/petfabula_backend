package com.petfabula.presentation.web.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.petfabula.presentation.web.api.Response;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class UnauthenticatedRequestHandler implements AuthenticationEntryPoint {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());

        Response res = Response.failedOnValidation();
        res.setCode(Response.ResponseCode.LOGIN_REQUIRED.getCode());
        res.setMessage(Response.ResponseCode.LOGIN_REQUIRED.getMessage());

        response.getOutputStream()
                .println(objectMapper.writeValueAsString(res));
    }
}
