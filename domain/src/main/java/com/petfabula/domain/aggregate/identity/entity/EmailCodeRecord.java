package com.petfabula.domain.aggregate.identity.entity;


import com.petfabula.domain.aggregate.identity.service.PasswordEncoderService;
import com.petfabula.domain.common.domain.EntityBase;
import com.petfabula.domain.common.validation.EntityValidationUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import java.time.Instant;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "identity_email_code_record",
        indexes = {@Index(name = "email_index",  columnList="email")})
public class EmailCodeRecord extends EntityBase {

    public static final Instant PERMANENT_EXPIRATION = Instant.EPOCH;

    public EmailCodeRecord(Long userId, String email, String code) {
        setId(userId);
        setEmail(email);
        setCode(code);
        setActive(true);
    }

    @Column(name = "email", unique = true, nullable = false, length = 128)
    private String email;

    @Column(name = "code", nullable = false, length = 128)
    private String code;

    @Column(name = "active", nullable = false)
    private boolean active;

    @Column(name = "expiration")
    private Instant expiration;

    public void setEmail(String email) {
        EntityValidationUtils.validEmail("email", email);
        this.email = email;
    }

    public void setCode(String code) {
        EntityValidationUtils.validStringLength("code", code, 6, 6);
        this.code = code;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setExpiration(Instant expiration) {
        this.expiration = expiration;
    }

    public boolean isValid() {
        if (!active) {
            return false;
        }
        if (expiration == null) {
            return true;
        }

        if (expiration.equals(PERMANENT_EXPIRATION)) {
            return true;
        }
        return expiration.isAfter(Instant.now());
    }
}
