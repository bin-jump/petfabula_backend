package com.petfabula.presentation.web.security.filter;

import com.petfabula.presentation.web.authentication.AuthenticationHelper;
import com.petfabula.presentation.web.authentication.LoginUser;
import com.petfabula.presentation.web.authentication.jwt.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// only for jwt
@Slf4j
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    @Autowired
    private AuthenticationHelper authenticationHelper;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {
        try{

            String token = request.getHeader(JwtUtils.JWT_HEADER_NAME);
            if (token == null) {
                Cookie[] cookies = request.getCookies();
                if (cookies != null) {
                    for (Cookie cookie : cookies) {
                        if (JwtUtils.JWT_COOKIE_NAME.equals(cookie.getName())) {
                            token = cookie.getValue();
                        }
                    }
                }
            }
            log.info("jwt token: " + token);

            LoginUser loginUser = authenticationHelper.loginUserFromToken(token);
            if (loginUser != null) {
                // TODO: fill AuthorityList
                List<SimpleGrantedAuthority> roles = new ArrayList<>();
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(loginUser, null, roles);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

            chain.doFilter(request, response);
        } finally {
            SecurityContextHolder.clearContext();
        }

    }

}

