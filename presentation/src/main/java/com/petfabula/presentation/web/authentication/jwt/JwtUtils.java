package com.petfabula.presentation.web.authentication.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.petfabula.presentation.web.authentication.LoginUser;

import java.util.Date;

public final class JwtUtils {

    public static final String CLAIM_EMAIL = "email";

    public static final String CLAIM_USER_ID = "userId";

    public static final String JWT_COOKIE_NAME = "AUTH_TOKEN";

    public static final String JWT_HEADER_NAME = "Authorization";

    public static String generateUserToken(String signSecret, LoginUser loginUser, long duration) {

        String token = JWT.create()
                .withClaim(CLAIM_USER_ID, loginUser.getUserId())
                .withExpiresAt(new Date(System.currentTimeMillis() + duration))
                .sign(Algorithm.HMAC512(signSecret.getBytes()));

        return token;
    }

    public static LoginUser getLoginUser(String signSecret, String token) {
        Algorithm algorithm = Algorithm.HMAC512(signSecret.getBytes());
        JWTVerifier verifier = JWT.require(algorithm).build();

        DecodedJWT jwt = verifier.verify(token);
        Long id = jwt.getClaim(CLAIM_USER_ID).asLong();

        LoginUser loginUser = LoginUser.builder()
                .userId(id)
                .build();
        return loginUser;

    }
}
