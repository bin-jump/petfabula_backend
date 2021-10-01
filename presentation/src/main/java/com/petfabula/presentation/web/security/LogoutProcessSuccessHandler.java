package com.petfabula.presentation.web.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.petfabula.presentation.web.api.Response;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LogoutProcessSuccessHandler implements LogoutSuccessHandler {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {

        Response res = Response.ok();
        httpServletResponse.getOutputStream()
                .println(objectMapper.writeValueAsString(res));
    }
}
