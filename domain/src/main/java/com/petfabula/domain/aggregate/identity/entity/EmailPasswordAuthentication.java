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

@NoArgsConstructor
@Getter
@Entity
@Table(name = "identity_email_password_authentication",
        indexes = {@Index(name = "email_index",  columnList="email", unique = true)})
public class EmailPasswordAuthentication extends EntityBase {

    public EmailPasswordAuthentication(Long userId, String email, String password) {
        setId(userId);
        setEmail(email);
        setPassword(password);
    }

    @Column(name = "email", unique = true, nullable = false, length = 128)
    private String email;

    @Column(name = "password", nullable = false, length = 128)
    private String password;

    public void setEmail(String email) {
        EntityValidationUtils.validEmail("email", email);
        this.email = email;
    }

    public void setPassword(String password) {
        EntityValidationUtils.validPassword("password", password);

        this.password = PasswordEncoderService.newInstance().encode(password);
    }
}
