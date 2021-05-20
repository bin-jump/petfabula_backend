package com.petfabula.domain.aggregate.identity.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class PasswordEncoderService {

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public static PasswordEncoderService newInstance() {
        return new PasswordEncoderService();
    }

    public PasswordEncoder getInternalEncoder() {
        return passwordEncoder;
    }

    public String encode(String password) {
        return passwordEncoder.encode(password);
    }

    public boolean check(String encoded, String target) {
        return passwordEncoder.matches(target, encoded);
    }

}
