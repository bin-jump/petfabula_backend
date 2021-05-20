package com.petfabula.domain.aggregate.identity.entity;

import javax.persistence.Column;

public class PhoneAuthentication {

    @Column(name = "phone_number", unique = true, length = 16)
    private String phoneNumber;
}
