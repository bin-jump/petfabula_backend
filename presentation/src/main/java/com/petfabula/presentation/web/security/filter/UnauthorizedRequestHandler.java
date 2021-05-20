package com.petfabula.presentation.web.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.petfabula.presentation.web.api.Response;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class UnauthorizedRequestHandler implements AccessDeniedHandler {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException, ServletException {
        response.setStatus(HttpStatus.FORBIDDEN.value());

        Response res = Response.failedOnValidation();
        res.setCode(Response.ResponseCode.NO_PERMISSION.getCode());
        res.setMessage(Response.ResponseCode.NO_PERMISSION.getMessage());

        response.getOutputStream()
                .println(objectMapper.writeValueAsString(res));
    }
}
