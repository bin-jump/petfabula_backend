package com.petfabula.domain.aggregate.identity.entity;

import com.petfabula.domain.base.EntityBase;
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
@Table(name = "identity_email_code_authentication",
        indexes = {@Index(name = "email_index",  columnList="email", unique = true)})
public class EmailCodeAuthentication extends EntityBase  {

    public EmailCodeAuthentication(Long userId, String email) {
        setId(userId);
        setEmail(email);
    }

    @Column(name = "email", unique = true, nullable = false, length = 128)
    private String email;

    public void setEmail(String email) {
        EntityValidationUtils.validEmail("email", email);
        this.email = email;
    }
}
