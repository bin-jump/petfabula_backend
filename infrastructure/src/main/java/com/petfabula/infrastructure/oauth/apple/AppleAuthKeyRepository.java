package com.petfabula.infrastructure.oauth.apple;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class AppleAuthKeyRepository {

    private static String APPLE_AUTH_URL = "https://appleid.apple.com/auth/keys";

    @Autowired
    private RestTemplate restTemplate;

    public AppleAuthKey getKeyById(String kid) {
        ResponseEntity<KeyResponse> response
                = restTemplate.getForEntity(APPLE_AUTH_URL, KeyResponse.class);

        for(AppleAuthKey k : response.getBody().getKeys()) {
            if (k.getKid().equals(kid)) {
                return k;
            }
        }

        return null;
    }
}
