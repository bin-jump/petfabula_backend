package com.petfabula.presentation.web.security;

import com.petfabula.presentation.web.authentication.LoginUser;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class LoginUtils {

    public static Long currentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof AnonymousAuthenticationToken) {
            return null;
        }

        LoginUser userInfo = (LoginUser) authentication.getPrincipal();
        return userInfo.getUserId();
    }
}
