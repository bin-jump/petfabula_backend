package com.petfabula.presentation.web.authentication;

import com.petfabula.domain.aggregate.identity.entity.Role;
import com.petfabula.domain.aggregate.identity.entity.UserAccount;
import com.petfabula.presentation.web.authentication.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.session.ConcurrentSessionControlAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class AuthenticationHelper {

    public static final int LOGIN_DURATION_DAY = 60;

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private AuthenticationProps authenticationProps;

    public LoginUser loginUserFromToken(String token) {
        if (token == null) {
            return null;
        }

        return JwtUtils.getLoginUser(authenticationProps.getSignSecret(), token);
    }

    public void signin(UserAccount userAccount, HttpServletRequest req) {

        Set<Role> roles = userAccount.getRoles();
//        String[] names = Arrays.stream(roleNames).toArray(String[]::new);

        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        roles.stream().forEach(item ->
                authorities.add(new SimpleGrantedAuthority("ROLE_" + item.getName())));
        Authentication authentication = new UsernamePasswordAuthenticationToken(LoginUser.newInstance(userAccount), null,
                authorities);
//        authentication.setAuthenticated(true);

        SecurityContextHolder.getContext().setAuthentication(authentication);
//        HttpSession session = req.getSession(true);
//        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());
    }

//    public void logout(HttpServletResponse response) {
//
//    }

//    public void loginUser(UserAccount userAccount, HttpServletResponse response) {
//        long duration = TimeUnit.HOURS.toMillis(LOGIN_DURATION_DAY * 24);
//        int maxAge = (int) (duration / 1000);
//
//        String token = JwtUtils.generateUserToken(authenticationProps.getSignSecret(),
//                LoginUser.newInstance(userAccount), duration);
//        ResponseCookie cookie = ResponseCookie.from(JwtUtils.JWT_COOKIE_NAME, token)
//                .maxAge(maxAge)
//                //.domain("")
//                .sameSite("None")
//                //.secure(true)
//                .httpOnly(true)
//                .path("/")
//                .build();
//        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
//        response.setHeader(JwtUtils.JWT_HEADER_NAME, token);
//    }

    // for refreshing jwt expire date
//    public void refresh(LoginUser loginUser, HttpServletResponse response) {
//        long duration = TimeUnit.HOURS.toMillis(LOGIN_DURATION_DAY * 24);
//        int maxAge = (int) (duration / 1000);
//
//        String token = JwtUtils
//                .generateUserToken(authenticationProps.getSignSecret(), loginUser, duration);
//        ResponseCookie cookie = ResponseCookie.from(JwtUtils.JWT_COOKIE_NAME, token)
//                .maxAge(maxAge)
//                .sameSite("None")
//                .httpOnly(true)
//                .path("/")
//                .build();
//        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
//        response.setHeader(JwtUtils.JWT_HEADER_NAME, token);
//    }


//    public void logout(HttpServletResponse response) {
//
//        ResponseCookie cookie = ResponseCookie.from(JwtUtils.JWT_COOKIE_NAME, "")
//                .maxAge(0)
//                //.domain("")
//                .httpOnly(true)
//                .sameSite("None")
//                //.secure(true)
//                .path("/")
//                .build();
//        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
//    }
}
