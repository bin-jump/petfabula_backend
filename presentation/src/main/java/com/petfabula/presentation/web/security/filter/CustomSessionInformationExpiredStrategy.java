package com.petfabula.presentation.web.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.petfabula.presentation.web.api.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.session.SessionInformationExpiredEvent;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomSessionInformationExpiredStrategy implements SessionInformationExpiredStrategy {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void onExpiredSessionDetected(SessionInformationExpiredEvent sessionInformationExpiredEvent) throws IOException, ServletException {

        HttpServletResponse response = sessionInformationExpiredEvent.getResponse();
        response.setStatus(HttpStatus.UNAUTHORIZED.value());

        Response res = Response.failedOnValidation();
        res.setCode(Response.ResponseCode.LOGIN_REQUIRED.getCode());
        res.setMessage(Response.ResponseCode.LOGIN_REQUIRED.getMessage());

        response.getOutputStream()
                .println(objectMapper.writeValueAsString(res));
    }

}