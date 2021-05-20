package com.petfabula.domain.exception;

import lombok.Getter;

@Getter
public class InvalidValueException extends DomainBaseException {

    private String name;

    private String msg;

    public InvalidValueException(String name, String msg) {
        super(String.format("field %s: %s", name, msg));
        this.name = name;
        this.msg = msg;

    }

}
