package com.petfabula.infrastructure.oauth.apple;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;

@Component
public class AppleJwtDecoder {

    @Autowired
    private AppleAuthKeyRepository keyRepository;

    @Autowired
    private ObjectMapper objectMapper;

    public AppleJwtPayload decode(String jwtToken) {
        AppleAuthKey k = getKey(jwtToken);

        PublicKey publicKey = getPublicKey(k);

        try{
            Claims claims = Jwts.parser()
//                    .setAllowedClockSkewSeconds(Long.MAX_VALUE)
                    .setSigningKey(publicKey)
                    .parseClaimsJws(jwtToken)
                    .getBody();

            String sub = claims.get("sub", String.class);
            String email = claims.get("email", String.class);
            String emailVerified = claims.get("email_verified", String.class);
            String realUserStatus = claims.get("real_user_status", String.class);

            return new AppleJwtPayload(sub, email, Boolean.parseBoolean(emailVerified),
                    realUserStatus != null ? Integer.parseInt(realUserStatus) : null);

        }catch(ExpiredJwtException e){
            throw new RuntimeException("apple token expired");
        }
    }

    private PublicKey getPublicKey(AppleAuthKey key) {
        BigInteger n = new BigInteger(1, Base64.getUrlDecoder().decode(key.getN()));
        BigInteger e = new BigInteger(1, Base64.getUrlDecoder().decode(key.getE()));

        KeyFactory keyFactory = null;
        try {
            keyFactory = KeyFactory.getInstance("RSA");

            KeySpec publicKeySpec = new RSAPublicKeySpec(n, e);
            PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);
            return publicKey;

        } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
            throw new RuntimeException("apple key creation error");
        }
    }

    private AppleAuthKey getKey(String jwtToken) {
        String kid = getKeyId(jwtToken);
        AppleAuthKey k = keyRepository.getKeyById(kid);

        if (k == null) {
            throw new RuntimeException("apple key error");
        }

        return k;
    }

    private String getKeyId(String jwtToken) {
        String[] chunks = jwtToken.split("\\.");
        Base64.Decoder decoder = Base64.getDecoder();
        String header = new String(decoder.decode(chunks[0]));

        try {
            JsonNode jsonNode = objectMapper.readTree(header);
            String kid = jsonNode.get("kid").asText();

            return kid;
        } catch (JsonProcessingException e) {
            throw new RuntimeException("apple jwt error");
        }
    }
}
