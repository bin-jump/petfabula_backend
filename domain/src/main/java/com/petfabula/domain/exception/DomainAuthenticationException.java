package com.petfabula.domain.exception;

import lombok.Getter;

@Getter
public class DomainAuthenticationException extends DomainBaseException {

    private String field;

    private String msg;

    public DomainAuthenticationException(String name, String msg) {
        super(String.format("field %s: %s", name, msg));
        this.field = name;
        this.msg = msg;

    }
}
