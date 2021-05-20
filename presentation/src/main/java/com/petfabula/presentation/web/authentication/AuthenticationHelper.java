package com.petfabula.presentation.web.authentication;

import com.petfabula.domain.aggregate.identity.entity.UserAccount;
import com.petfabula.presentation.web.authentication.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

@Component
public class AuthenticationHelper {

    public static final int LOGIN_DURATION_DAY = 60;

    @Autowired
    private AuthenticationProps authenticationProps;

    public LoginUser loginUserFromToken(String token) {
        if(token == null) {
            return null;
        }

        return JwtUtils.getLoginUser(authenticationProps.getSignSecret(), token);
    }

    public void loginUser(UserAccount userAccount, HttpServletResponse response) {
        long duration = TimeUnit.HOURS.toMillis(LOGIN_DURATION_DAY * 24);
        int maxAge = (int) (duration / 1000);

        String token = JwtUtils.generateUserToken(authenticationProps.getSignSecret(),
                LoginUser.newInstance(userAccount), duration);
        ResponseCookie cookie = ResponseCookie.from(JwtUtils.JWT_COOKIE_NAME, token)
                .maxAge(maxAge)
                //.domain("")
                .sameSite("None")
                //.secure(true)
                .httpOnly(true)
                .path("/")
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        response.setHeader(JwtUtils.JWT_HEADER_NAME, token);
    }

    // for refreshing jwt expire date
    public void refresh(LoginUser loginUser, HttpServletResponse response) {
        long duration = TimeUnit.HOURS.toMillis(LOGIN_DURATION_DAY * 24);
        int maxAge = (int) (duration / 1000);

        String token = JwtUtils
                .generateUserToken(authenticationProps.getSignSecret(), loginUser, duration);
        ResponseCookie cookie = ResponseCookie.from(JwtUtils.JWT_COOKIE_NAME, token)
                .maxAge(maxAge)
                .sameSite("None")
                .httpOnly(true)
                .path("/")
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        response.setHeader(JwtUtils.JWT_HEADER_NAME, token);
    }


    public void logout(HttpServletResponse response) {

        ResponseCookie cookie = ResponseCookie.from(JwtUtils.JWT_COOKIE_NAME, "")
                .maxAge(0)
                //.domain("")
                .httpOnly(true)
                .sameSite("None")
                //.secure(true)
                .path("/")
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }
}
